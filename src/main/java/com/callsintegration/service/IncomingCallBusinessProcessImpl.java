package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import com.callsintegration.dto.api.amocrm.*;
import com.callsintegration.dto.api.amocrm.response.AmoCRMCreatedEntityResponse;
import com.callsintegration.dto.api.amocrm.response.AmoCRMCreatedLeadsResponse;
import com.callsintegration.exception.APIAuthException;
import com.callsintegration.repository.CallRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            processCall(call);
            call.setState(Call.State.DONE);
            callRepository.save(call);
        } catch (APIAuthException e) {
            System.out.println("amoCRM auth error!!");
            e.printStackTrace();
        }
    }

    private void processCall(Call call) throws APIAuthException {
        String number = call.getNumber();
        List<AmoCRMContact> amoCRMContacts = amoCRMService.getContactsByQuery(number);

        if (amoCRMContacts.size() == 0) {
            log.info("Not found contacts for number " + number);

            /**
             * Удалено 02.12.2015 - теперь создавать отсутствующий контакт не надо.
             * Создание контактов осуществляет сторонний сервис
             */
            /*createContact(call);
            processCall(call);*/
        } else {
            log.info("Contacts found for number " + number);

            AmoCRMContact contact = null;
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
                            if(value.length() == 11){
                                value = value.substring(1);
                            }

                            if (value.equals(number)) {
                                contact = amoCRMContact;
                            }
                        }
                    }
                }
            }

            if (contact == null) {
                log.info("All found contacts for number " + number + " is wrong");
            } else {
                log.info("Got contact #" + contact.getId().toString() + " for number " + number + "!");
                this.workWithContact(contact, call);
            }
        }
    }

    private void workWithContact(AmoCRMContact contact, Call call) throws APIAuthException {
        String number = call.getNumber();
        ArrayList<Long> leadIds = contact.getLinked_leads_id();

        log.info("Work with contact #" + contact.getId());

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
        if (foundOpenedLeads == 0) {
            log.info("We need to create lead for contact #" + contact.getId());
            AmoCRMLead amoCRMLeadToWorkWith = this.createLeadForContact(contact, call);
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

    private void checkExistingLeadCustomFields(AmoCRMLead amoCRMLead, Call call) throws APIAuthException {

        log.info("checking custom fields for lead #".concat(amoCRMLead.getId().toString()));
        ArrayList<AmoCRMCustomField> amoCRMCustomFields = amoCRMLead.getCustom_fields();

        Boolean updated = false;

        if(amoCRMCustomFields == null){
            amoCRMCustomFields = new ArrayList<AmoCRMCustomField>();
        }

        AmoCRMCustomField marketingChannelCustomField = null;
        AmoCRMCustomField sourceCustomField = null;

        for(AmoCRMCustomField amoCRMCustomField : amoCRMCustomFields){

            if(amoCRMCustomField.getId().equals(this.getMarketingChannelLeadsCustomField())){
                // Кастомное поле "Рекламный канал"
                marketingChannelCustomField = amoCRMCustomField;
            }

            if(amoCRMCustomField.getId().equals(this.getSourceLeadsCustomField())){
                // Кастомное поле "Источник"
                sourceCustomField = amoCRMCustomField;
            }
        }

        if(
                marketingChannelCustomField == null ||
                        marketingChannelCustomField.getValues() == null ||
                        marketingChannelCustomField.getValues().size() == 0
                ){
            log.info("'Marketing Channel' is absent or empty");
            updated = true;
            String[] sourceField = {call.getSource()};
            amoCRMLead.addStringValuesToCustomField(this.getMarketingChannelLeadsCustomField(), sourceField);
        }

        if(
                sourceCustomField == null ||
                        sourceCustomField.getValues() == null ||
                        sourceCustomField.getValues().size() == 0
                ){
            log.info("'Source' is absent or empty");
            updated = true;
            String[] projectField = {this.getProjectIdToLeadsSource().get(call.getProjectId()).toString()};
            amoCRMLead.addStringValuesToCustomField(this.getSourceLeadsCustomField(), projectField);
        }


        if(updated){
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
        amoCRMLead.setResponsible_user_id(this.getDefaultUserId());
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

    private void createContact(Call call) throws APIAuthException {
        String number = call.getNumber();

        AmoCRMContact amoCRMContact = new AmoCRMContact();
        amoCRMContact.setName("CallTracking:[" + number + "]");
        amoCRMContact.setResponsible_user_id(this.getDefaultUserId());
        String[] fieldNumber = {number};
        amoCRMContact.addStringValuesToCustomField(this.getPhoneNumberCustomField(), fieldNumber);
        String[] fieldSource = {call.getSource()};
        amoCRMContact.addStringValuesToCustomField(this.getMarketingChannelContactsCustomField(), fieldSource);
        String[] fieldProject = {this.getProjectIdToContactsSource().get(call.getProjectId()).toString()};
        amoCRMContact.addStringValuesToCustomField(this.getSourceContactsCustomField(), fieldProject);

        amoCRMService.addContact(amoCRMContact);
    }

    public Long getPhoneNumberCustomField() {
        return phoneNumberCustomField;
    }

    public void setPhoneNumberCustomField(Long phoneNumberCustomField) {
        this.phoneNumberCustomField = phoneNumberCustomField;
    }

    public Long getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(Long defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    public Long getPhoneNumberCustomFieldLeads() {
        return phoneNumberCustomFieldLeads;
    }

    public void setPhoneNumberCustomFieldLeads(Long phoneNumberCustomFieldLeads) {
        this.phoneNumberCustomFieldLeads = phoneNumberCustomFieldLeads;
    }

    public Long getMarketingChannelContactsCustomField() {
        return marketingChannelContactsCustomField;
    }

    public void setMarketingChannelContactsCustomField(Long marketingChannelContactsCustomField) {
        this.marketingChannelContactsCustomField = marketingChannelContactsCustomField;
    }

    public Long getMarketingChannelLeadsCustomField() {
        return marketingChannelLeadsCustomField;
    }

    public void setMarketingChannelLeadsCustomField(Long marketingChannelLeadsCustomField) {
        this.marketingChannelLeadsCustomField = marketingChannelLeadsCustomField;
    }

    public Long getSourceContactsCustomField() {
        return sourceContactsCustomField;
    }

    public void setSourceContactsCustomField(Long sourceContactsCustomField) {
        this.sourceContactsCustomField = sourceContactsCustomField;
    }

    public Long getSourceLeadsCustomField() {
        return sourceLeadsCustomField;
    }

    public void setSourceLeadsCustomField(Long sourceLeadsCustomField) {
        this.sourceLeadsCustomField = sourceLeadsCustomField;
    }

    public HashMap<Integer, Long> getProjectIdToContactsSource() {
        return projectIdToContactsSource;
    }

    public void setProjectIdToContactsSource(HashMap<Integer, Long> projectIdToContactsSource) {
        this.projectIdToContactsSource = projectIdToContactsSource;
    }

    public HashMap<Integer, Long> getProjectIdToLeadsSource() {
        return projectIdToLeadsSource;
    }

    public void setProjectIdToLeadsSource(HashMap<Integer, Long> projectIdToLeadsSource) {
        this.projectIdToLeadsSource = projectIdToLeadsSource;
    }
}
