package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import com.callsintegration.dto.api.amocrm.*;
import com.callsintegration.dto.api.amocrm.response.AmoCRMCreatedEntityResponse;
import com.callsintegration.exception.APIAuthException;
import com.callsintegration.repository.CallRepository;
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CallRepository callRepository;

    @Autowired
    AmoCRMService amoCRMService;

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
    public void newIncomingCall(Call call){
        System.out.println("call from " + call.getNumber());

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

        if(amoCRMContacts.size() == 0){
            System.out.println("Not found contacts for this number");
            createContact(call);

            processCall(call);
        }
        else{
            System.out.println("Contacts found");

            AmoCRMContact contact = null;
            for(AmoCRMContact amoCRMContact : amoCRMContacts){
                if(amoCRMContact.getCustom_fields() == null)
                    continue;
                ArrayList<AmoCRMCustomField> crmCustomFields = amoCRMContact.getCustom_fields();

                for(AmoCRMCustomField amoCRMCustomField : crmCustomFields){
                    if(
                            amoCRMCustomField.getId().equals(this.getPhoneNumberCustomField()) ||
                                    (amoCRMCustomField.getCode() != null && amoCRMCustomField.getCode().equals("PHONE"))
                            ) {
                        System.out.println("Got custom field with phone number");

                        for (AmoCRMCustomFieldValue amoCRMCustomFieldValue : amoCRMCustomField.getValues()) {
                            if (amoCRMCustomFieldValue.getValue().equals(number)) {
                                System.out.println("Custom field has correct number");
                                contact = amoCRMContact;
                            }
                        }
                    }
                    // TODO для доп. телефонов определять, что это - телефон по полю code. А поле enum можно вообще не парсить, нам без разницы

                }
            }

            if(contact == null){
                System.out.println("All found contacts is wrong");
                createContact(call);
                processCall(call);
            }
            else{
                System.out.println("Got contact!");

                this.workWithContact(contact, call);
            }
        }
    }

    private void workWithContact(AmoCRMContact contact, Call call) throws APIAuthException {
        String number = call.getNumber();
        ArrayList<Long> leadIds = contact.getLinked_leads_id();

        System.out.println("Work with contact");

        AmoCRMLead amoCRMLeadFound = null;
        if(leadIds != null && leadIds.size() != 0){
            System.out.println("Leads found. Checking statuses");
            for(Long leadId : leadIds){
                System.out.println("Lead #" + leadId);
                AmoCRMLead amoCRMLead = amoCRMService.getLeadById(leadId);

                if(amoCRMLead != null){
                    if(amoCRMService.getLeadClosedStatusesIDs().contains(amoCRMLead.getStatus_id())){
                        System.out.println("Lead is closed");
                    }
                    else{
                        System.out.println("Lead is open!");
                        amoCRMLeadFound = amoCRMLead;
                    }
                }
            }
        }

        if(amoCRMLeadFound != null){

            // Добавляем звонок
            AmoCRMNote amoCRMNote = new AmoCRMNote();
            // 10 - звонок
            amoCRMNote.setNote_type(10);
            AmoCRMNoteText amoCRMNoteText = new AmoCRMNoteText();
            amoCRMNoteText.setPhone(number);
            amoCRMNoteText.setUniq(call.getId().toString());
            Map<String, String> params = call.getParams();
            //String number = call.getNumber();
            String duration = (params != null && params.get("ct:duration") != null)? params.get("ct:duration") : "1";
            amoCRMNoteText.setDuration(duration);
            String status = "4";
            if(call.getStatus() != null){
                switch (call.getStatus()){
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

            amoCRMService.addNoteToLead(amoCRMNote, amoCRMLeadFound);
        }

        else{
            System.out.println("We need to create lead for contact");
            this.createLeadForContact(contact, call);
        }
    }

    private void createLeadForContact(AmoCRMContact contact, Call call) throws APIAuthException {
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
        System.out.println("Creating lead for contact");
        AmoCRMCreatedEntityResponse amoCRMCreatedEntityResponse = amoCRMService.addLead(amoCRMLead);

        if(amoCRMCreatedEntityResponse == null){
            throw new IllegalStateException("No response, but we have not any error message from AmoCRM API!");
        }

        System.out.println("Adding contact to lead");

        amoCRMService.addContactToLead(contact, amoCRMService.getLeadById(amoCRMCreatedEntityResponse.getId()));
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
