package com.callsintegration.businessprocesses.processes;

import com.callsintegration.businessprocesses.rules.BusinessRulesValidator;
import com.callsintegration.dmodel.Call;
import com.callsintegration.dmodel.Site;
import com.callsintegration.dto.api.amocrm.*;
import com.callsintegration.dto.api.amocrm.response.AmoCRMCreatedContactsResponse;
import com.callsintegration.dto.api.amocrm.response.AmoCRMCreatedEntityResponse;
import com.callsintegration.dto.api.amocrm.response.AmoCRMCreatedLeadsResponse;
import com.callsintegration.exception.APIAuthException;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.repository.SiteRepository;
import com.callsintegration.service.AmoCRMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by berz on 10.10.2015.
 */
@Service
@Transactional
public class IncomingCallBusinessProcessImpl implements IncomingCallBusinessProcess {

    @Autowired
    CallRepository callRepository;

    @Autowired
    AmoCRMService amoCRMService;

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    BusinessRulesValidator businessRulesValidator;

    private static final boolean CREATE_TASK_FOR_EACH_CALL = true;
    private static final boolean CREATE_LEAD_IF_ABSENT = true;

    private static final Logger log = LoggerFactory.getLogger(IncomingCallBusinessProcessImpl.class);

    /*
     * Id пользователя по умолчанию, которому назначаются все сделки
     */
    private Long defaultUserId;

    /*
     * Id кастомных полей "номер телефона (API)" для контактов и сделок
     */
    private Long phoneNumberCustomField;
    private Long phoneNumberCustomFieldLeads;

    /*
    *
    * ID поля "телефон" и enum "Рабочий"
     */
    private Long phoneNumberContactStockField;

    private String phoneNumberStockFieldContactEnumWork;

    /**
     * Id поля "email" для контактов и сделок
     */
    private Long emailContactCustomField;
    private String emailContactEnum;

    /*
     * Id кастомных полей "Рекламный канал" для контактов и сделок
     */
    private Long marketingChannelContactsCustomField;
    private Long marketingChannelLeadsCustomField;

    /*
     * Id кастомных полей "Источник" для контактов и сделок
     */
    private Long sourceContactsCustomField;
    private Long sourceLeadsCustomField;

    /*
     * Привязки {id проекта calltracking}=>{enum значение поля "Источник"}
     */
    private HashMap<Integer, Long> projectIdToContactsSource;
    private HashMap<Integer, Long> projectIdToLeadsSource;

    @Override
    public void newIncomingCall(Call call) {
        log.info("Work with new call from number: " + call.getNumber());

        try {
            if(businessRulesValidator.validate(call)) {
                log.info("Call was succesfully validated!");
                processCall(call);
            }
            else{
                log.error("Call object has not pass validation!");
            }
            call.setState(Call.State.DONE);
            callRepository.save(call);
        } catch (APIAuthException e) {
            System.out.println("amoCRM auth error!!");
            e.printStackTrace();
        }
    }

    private void processCall(Call call) throws APIAuthException {
        AmoCRMContact contact = null;

        String number = call.getNumber();
        List<AmoCRMContact> amoCRMContacts = amoCRMService.getContactsByQuery(number);

        if (amoCRMContacts.size() == 0) {
            log.info("Not found contacts for number " + number + "; creating new");
            contact = createContact(call);
        } else {
            log.info("Contacts found for number " + number + ": " + amoCRMContacts.size());

            for (AmoCRMContact amoCRMContact : amoCRMContacts) {
                if (amoCRMContact.getCustom_fields() == null)
                    continue;
                ArrayList<AmoCRMCustomField> crmCustomFields = amoCRMContact.getCustom_fields();

                for (AmoCRMCustomField amoCRMCustomField : crmCustomFields) {
                    if (
                            amoCRMCustomField.getId().equals(this.getPhoneNumberCustomField()) ||
                                    (amoCRMCustomField.getCode() != null && amoCRMCustomField.getCode().equals("PHONE"))
                            ) {

                        for (AmoCRMCustomFieldValue amoCRMCustomFieldValue : amoCRMCustomField.getValues()) {
                            String value = amoCRMCustomFieldValue.getValue();
                            if (value.length() == 11) {
                                value = value.substring(1);
                            }

                            if (value.equals(number)) {
                                contact = amoCRMContact;
                            }
                        }
                    }
                }
            }
        }

        if (contact == null) {
            log.info("All found contacts for number " + number + " is wrong");
            throw new IllegalStateException("All found contacts for number " + number + " is wrong");
        } else {
            log.info("Got contact #" + contact.getId().toString() + " for number " + number + "!");
            this.workWithContact(contact, call);
        }
    }

    private void workWithContact(AmoCRMContact contact, Call call) throws APIAuthException {
        String number = call.getNumber();
        ArrayList<Long> leadIds = contact.getLinked_leads_id();

        log.info("Work with contact #" + contact.getId());
        checkExistingContactCustomFields(contact, call);

        Integer foundOpenedLeads = 0;
        if (leadIds != null && leadIds.size() != 0) {
            log.info("Leads found. Checking statuses");
            for (Long leadId : leadIds) {
                log.info("Lead #" + leadId);
                AmoCRMLead amoCRMLead = amoCRMService.getLeadById(leadId);

                if (amoCRMLead != null) {
                    if (amoCRMService.getLeadClosedStatusesIDs().contains(amoCRMLead.getStatus_id())) {
                        log.info("Lead is closed");
                    } else {
                        log.info("Lead is open!");
                        //amoCRMLeadToWorkWith = amoCRMLead;
                        foundOpenedLeads++;
                        checkExistingLeadCustomFields(amoCRMLead, call);
                    }
                }
            }
        }

        // Не найдено сделок
        if (foundOpenedLeads == 0 && CREATE_LEAD_IF_ABSENT) {
            log.info("We need to create lead for contact #" + contact.getId());
            AmoCRMLead amoCRMLeadToWorkWith = this.createLeadForContact(contact, call);
        }
        else{
            log.info("Found leads for contact #" + contact.getId() + ". We need to create task for new call");
            this.createTasksForCall(contact, call);
        }


        /*
        *
        * Добавлять событие "звонок" больше не нужно.
        * 21.01.2016
         */
        /*
        // Добавляем звонок
        AmoCRMNote amoCRMNote = new AmoCRMNote();
        // 10 - звонок
        amoCRMNote.setNote_type(10);
        AmoCRMNoteText amoCRMNoteText = new AmoCRMNoteText();
        amoCRMNoteText.setPhone(number);
        amoCRMNoteText.setUniq(call.getId().toString());
        Map<String, String> params = call.getParams();
        //String number = call.getNumber();
        String duration = (params != null && params.get("ct:duration") != null) ? params.get("ct:duration") : "1";
        amoCRMNoteText.setDuration(duration);
        String status = "4";
        if (call.getStatus() != null) {
            switch (call.getStatus()) {
                case ANSWERED:
                    status = "4";
                    break;
                case NO_ANSWER:
                    status = "2";
                    break;
                case BUSY:
                    status = "6";
            }
            amoCRMNoteText.setCall_result(call.getStatus().getStr());
        }
        amoCRMNoteText.setCall_status(status);
        amoCRMNoteText.setSrc("CallTracking");
        amoCRMNote.setText(amoCRMNoteText);

        amoCRMService.addNoteToLead(amoCRMNote, amoCRMLeadToWorkWith);
        */
    }

    private void createTasksForCall(AmoCRMContact contact, Call call) throws APIAuthException {
        if(CREATE_TASK_FOR_EACH_CALL) {
            AmoCRMTask amoCRMTask = new AmoCRMTask();
            amoCRMTask.setResponsible_user_id(getDefaultUserId());
            amoCRMTask.setContact(contact);

            // Связаться с клиентом
            amoCRMTask.setTask_type(1l);
            amoCRMTask.setText("Повторный звонок от клиента!");
            amoCRMTask.setComplete_till(new Date());

            amoCRMService.addTask(amoCRMTask);
        }
    }

    private void checkExistingContactCustomFields(AmoCRMContact contact, Call call) throws APIAuthException {
        log.info("checking custom fileds for contact #".concat(contact.getId().toString()));

        ArrayList<AmoCRMCustomField> crmCustomFields = contact.getCustom_fields();
        Boolean updated = false;

        if(crmCustomFields == null){
            crmCustomFields = new ArrayList<>();
        }

        AmoCRMCustomField marketingChannelCustomField = null;
        AmoCRMCustomField sourceCustomField = null;

        for (AmoCRMCustomField amoCRMCustomField : crmCustomFields) {

            if (amoCRMCustomField.getId().equals(this.getMarketingChannelContactsCustomField())) {
                // Кастомное поле "Рекламный канал"
                marketingChannelCustomField = amoCRMCustomField;
            }

            if (amoCRMCustomField.getId().equals(this.getSourceContactsCustomField())) {
                // Кастомное поле "Источник"
                sourceCustomField = amoCRMCustomField;
            }
        }

        if (
                marketingChannelCustomField == null ||
                        marketingChannelCustomField.getValues() == null ||
                        marketingChannelCustomField.getValues().size() == 0
                ) {
            log.info("'Marketing Channel' is absent or empty");
            if(call.getSource() != null) {
                updated = true;
                String[] sourceField = {call.getSource()};
                contact.addStringValuesToCustomField(this.getMarketingChannelContactsCustomField(), sourceField);
            }
            else{
                log.info("to be honest, sourceField is empty in Call entity too. This is the life");
            }
        }

        if (
                sourceCustomField == null ||
                        sourceCustomField.getValues() == null ||
                        sourceCustomField.getValues().size() == 0
                ) {
            log.info("'Source' is absent or empty");

            if(call.getProjectId() != null && this.getProjectIdToContactsSource().get(call.getProjectId()) != null) {
                updated = true;
                String[] projectField = {this.getProjectIdToContactsSource().get(call.getProjectId()).toString()};
                contact.addStringValuesToCustomField(this.getSourceContactsCustomField(), projectField);
            }
            else{
                log.info("to be honest, projectID is empty in Call entity too or this project not properly registered. This is the life");
            }
        }

        if (updated) {
            log.info("Contact #".concat(contact.getId().toString()).concat(" has been updated"));

            AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
            ArrayList<AmoCRMContact> amoCRMContacts = new ArrayList<>();
            amoCRMContacts.add(contact);
            amoCRMEntities.setUpdate(amoCRMContacts);
            AmoCRMCreatedContactsResponse response = amoCRMService.editContacts(amoCRMEntities);
            if (response == null) {
                throw new IllegalStateException("No response, but we have not any error message from AmoCRM API!");
            }

            log.info("updating contact resonse: " + response.getResponse().toString());
        }

    }


    private void checkExistingLeadCustomFields(AmoCRMLead amoCRMLead, Call call) throws APIAuthException {

        log.info("checking custom fields for lead #".concat(amoCRMLead.getId().toString()));
        ArrayList<AmoCRMCustomField> amoCRMCustomFields = amoCRMLead.getCustom_fields();

        Boolean updated = false;

        if (amoCRMCustomFields == null) {
            amoCRMCustomFields = new ArrayList<AmoCRMCustomField>();
        }

        AmoCRMCustomField marketingChannelCustomField = null;
        AmoCRMCustomField sourceCustomField = null;

        for (AmoCRMCustomField amoCRMCustomField : amoCRMCustomFields) {

            if (amoCRMCustomField.getId().equals(this.getMarketingChannelLeadsCustomField())) {
                // Кастомное поле "Рекламный канал"
                marketingChannelCustomField = amoCRMCustomField;
            }

            if (amoCRMCustomField.getId().equals(this.getSourceLeadsCustomField())) {
                // Кастомное поле "Источник"
                sourceCustomField = amoCRMCustomField;
            }
        }

        if (
                marketingChannelCustomField == null ||
                        marketingChannelCustomField.getValues() == null ||
                        marketingChannelCustomField.getValues().size() == 0
                ) {
            log.info("'Marketing Channel' is absent or empty");
            if(call.getSource() != null) {
                updated = true;
                String[] sourceField = {call.getSource()};
                amoCRMLead.addStringValuesToCustomField(this.getMarketingChannelLeadsCustomField(), sourceField);
            }
            else{
                log.info("to be honest, sourceField is empty in Call entity too. This is the life");
            }
        }

        if (
                sourceCustomField == null ||
                        sourceCustomField.getValues() == null ||
                        sourceCustomField.getValues().size() == 0
                ) {
            log.info("'Source' is absent or empty");

            if(call.getProjectId() != null && this.getProjectIdToLeadsSource().get(call.getProjectId()) != null) {
                updated = true;
                String[] projectField = {this.getProjectIdToLeadsSource().get(call.getProjectId()).toString()};
                amoCRMLead.addStringValuesToCustomField(this.getSourceLeadsCustomField(), projectField);
            }
            else{
                log.info("to be honest, projectID is empty in Call entity too or this project not properly registered. This is the life");
            }
        }

        if (updated) {
            log.info("Lead #".concat(amoCRMLead.getId().toString()).concat(" has been updated"));

            AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
            ArrayList<AmoCRMLead> amoCRMLeads = new ArrayList<>();
            amoCRMLeads.add(amoCRMLead);
            amoCRMEntities.setUpdate(amoCRMLeads);
            AmoCRMCreatedLeadsResponse response = amoCRMService.editLeads(amoCRMEntities);
            if (response == null) {
                throw new IllegalStateException("No response, but we have not any error message from AmoCRM API!");
            }

            log.info(response.getResponse().toString());
        }
    }

    private AmoCRMLead createLeadForContact(AmoCRMContact contact, Call call) throws APIAuthException {

        String number = call.getNumber();
        AmoCRMLead amoCRMLead = new AmoCRMLead();
        amoCRMLead.setName("Автоматически -> " + contact.getName());

        Long responsibleUserID = contact.getResponsible_user_id() != null ? contact.getResponsible_user_id() : this.getDefaultUserId();

        amoCRMLead.setResponsible_user_id(responsibleUserID);
        String[] numberField = {number};
        amoCRMLead.addStringValuesToCustomField(this.getPhoneNumberCustomFieldLeads(), numberField);
        String[] sourceField = {call.getSource()};
        amoCRMLead.addStringValuesToCustomField(this.getMarketingChannelLeadsCustomField(), sourceField);
        String[] projectField = {this.getProjectIdToLeadsSource().get(call.getProjectId()).toString()};
        amoCRMLead.addStringValuesToCustomField(this.getSourceLeadsCustomField(), projectField);
        log.info("Creating lead for contact #" + contact.getId());

        AmoCRMCreatedEntityResponse amoCRMCreatedEntityResponse = amoCRMService.addLead(amoCRMLead);


        if (amoCRMCreatedEntityResponse == null) {
            throw new IllegalStateException("No response, but we have not any error message from AmoCRM API!");
        }

        // Обновляем данные
        AmoCRMLead amoCRMLead1 = amoCRMService.getLeadById(amoCRMCreatedEntityResponse.getId());
        log.info("Adding contact #" + contact.getId() + " to lead #" + amoCRMLead1.getId());
        amoCRMService.addContactToLead(contact, amoCRMLead1);

        return amoCRMLead1;
    }

    private AmoCRMContact createContact(Call call) throws APIAuthException {
        String number = call.getNumber();

        AmoCRMContact amoCRMContact = new AmoCRMContact();
        amoCRMContact.setName("CallTracking:[" + number + "]");
        amoCRMContact.setResponsible_user_id(this.getDefaultUserId());
        String[] fieldNumber = {number};
        String[] extFieldNumber = {"7" + number};
        amoCRMContact.addStringValuesToCustomField(this.getPhoneNumberCustomField(), fieldNumber);
        amoCRMContact.addStringValuesToCustomField(this.getPhoneNumberContactStockField(), extFieldNumber, this.getPhoneNumberStockFieldContactEnumWork());
        String[] fieldSource = {call.getSource()};
        amoCRMContact.addStringValuesToCustomField(this.getMarketingChannelContactsCustomField(), fieldSource);
        String[] fieldProject = {this.getProjectIdToContactsSource().get(call.getProjectId()).toString()};
        amoCRMContact.addStringValuesToCustomField(this.getSourceContactsCustomField(), fieldProject);

        AmoCRMCreatedEntityResponse resp = amoCRMService.addContact(amoCRMContact);
        if(resp.getId() == null){
            throw new IllegalStateException("Request to create contact was sent but no `id created` returned");
        }
        AmoCRMContact contact = amoCRMService.getContactById(resp.getId());
        return contact;
    }


    @Override
    public Long getPhoneNumberCustomField() {
        return phoneNumberCustomField;
    }

    public void setPhoneNumberCustomField(Long phoneNumberCustomField) {
        this.phoneNumberCustomField = phoneNumberCustomField;
    }

    @Override
    public Long getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(Long defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    @Override
    public Long getPhoneNumberCustomFieldLeads() {
        return phoneNumberCustomFieldLeads;
    }

    public void setPhoneNumberCustomFieldLeads(Long phoneNumberCustomFieldLeads) {
        this.phoneNumberCustomFieldLeads = phoneNumberCustomFieldLeads;
    }

    @Override
    public Long getMarketingChannelContactsCustomField() {
        return marketingChannelContactsCustomField;
    }

    public void setMarketingChannelContactsCustomField(Long marketingChannelContactsCustomField) {
        this.marketingChannelContactsCustomField = marketingChannelContactsCustomField;
    }

    @Override
    public Long getMarketingChannelLeadsCustomField() {
        return marketingChannelLeadsCustomField;
    }

    public void setMarketingChannelLeadsCustomField(Long marketingChannelLeadsCustomField) {
        this.marketingChannelLeadsCustomField = marketingChannelLeadsCustomField;
    }

    @Override
    public Long getSourceContactsCustomField() {
        return sourceContactsCustomField;
    }

    public void setSourceContactsCustomField(Long sourceContactsCustomField) {
        this.sourceContactsCustomField = sourceContactsCustomField;
    }

    @Override
    public Long getSourceLeadsCustomField() {
        return sourceLeadsCustomField;
    }

    public void setSourceLeadsCustomField(Long sourceLeadsCustomField) {
        this.sourceLeadsCustomField = sourceLeadsCustomField;
    }

/*
    public HashMap<Integer, Long> getProjectIdToContactsSource() {
        return projectIdToContactsSource;
    }

    public void setProjectIdToContactsSource(HashMap<Integer, Long> projectIdToContactsSource) {
        this.projectIdToContactsSource = projectIdToContactsSource;
    }
*/

    public HashMap<Integer, Long> getProjectIdToLeadsSource() {
        if(projectIdToLeadsSource == null){
            this.setProjectIdToLeadsSource(new HashMap<>());
        }


        if(projectIdToLeadsSource.size() == 0){
            log.info("updating projectIdToLeadsSource in incomingBusinessProcessImpl");
            List<Site> sites = (List<Site>) siteRepository.findAll();
            for(Site site : sites){
                projectIdToLeadsSource.put(site.getCallTrackingProjectId(), Long.decode(site.getCrmLeadSourceId()));
            }
        }
        return projectIdToLeadsSource;
    }

    public HashMap<Integer, Long> getProjectIdToContactsSource() {
        if(projectIdToContactsSource == null){
            this.setProjectIdToContactsSource(new HashMap<>());
        }

        if(projectIdToContactsSource.size() == 0){
            log.info("updating projectIdToContactsSource in incomingBusinessProcessImpl");
            List<Site> sites = (List<Site>) siteRepository.findAll();
            for(Site site : sites){
                projectIdToContactsSource.put(site.getCallTrackingProjectId(), Long.decode(site.getCrmContactSourceId()));
            }
        }

        return projectIdToContactsSource;
    }

    public void setProjectIdToLeadsSource(HashMap<Integer, Long> projectIdToLeadsSource) {
        this.projectIdToLeadsSource = projectIdToLeadsSource;
    }

    public void setEmailContactCustomField(Long emailContactCustomField) {
        this.emailContactCustomField = emailContactCustomField;
    }

    @Override
    public Long getEmailContactCustomField() {
        return emailContactCustomField;
    }

    @Override
    public String getEmailContactEnum() {
        return emailContactEnum;
    }

    @Override
    public void setEmailContactEnum(String emailContactEnum) {
        this.emailContactEnum = emailContactEnum;
    }


    public void setProjectIdToContactsSource(HashMap<Integer, Long> projectIdToContactsSource) {
        this.projectIdToContactsSource = projectIdToContactsSource;
    }

    public Long getPhoneNumberContactStockField() {
        return phoneNumberContactStockField;
    }

    @Override
    public void setPhoneNumberContactStockField(Long phoneNumberContactStockField) {
        this.phoneNumberContactStockField = phoneNumberContactStockField;
    }

    public String getPhoneNumberStockFieldContactEnumWork() {
        return phoneNumberStockFieldContactEnumWork;
    }

    @Override
    public void setPhoneNumberStockFieldContactEnumWork(String phoneNumberStockFieldContactEnumWork) {
        this.phoneNumberStockFieldContactEnumWork = phoneNumberStockFieldContactEnumWork;
    }

}
