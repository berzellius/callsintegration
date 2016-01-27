package com.callsintegration.service;

import com.callsintegration.dto.api.amocrm.AmoCRMCustomField;
import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.callsintegration.dto.api.amocrm.AmoCRMNote;
import com.callsintegration.dto.api.amocrm.AmoCRMNoteText;
import com.callsintegration.exception.APIAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by berz on 06.11.2015.
 */
@Service
public class AddingCallNotesToEmptyLeadImpl implements AddingCallNotesToEmptyLead {

    @Autowired
    AmoCRMService amoCRMService;

    private Long phoneNumberCustomFieldLeads;

    @Override
    public void start(){
        System.out.println("Start AddingCallNotesToEmptyLead");

        try {
            List<AmoCRMLead> leads = amoCRMService.getLeadsByQuery("");

            for(AmoCRMLead amoCRMLead : leads){
                AmoCRMCustomField apiPhoneNumberCustomField = null;

                ArrayList<AmoCRMCustomField> crmCustomFields = amoCRMLead.getCustom_fields();
                if(crmCustomFields == null) continue;
                for(AmoCRMCustomField amoCRMCustomField : crmCustomFields){
                    if(amoCRMCustomField.getId().equals(this.phoneNumberCustomFieldLeads)){
                        apiPhoneNumberCustomField = amoCRMCustomField;
                    }
                }

                if(apiPhoneNumberCustomField != null){
                    List<AmoCRMNote> notes = amoCRMService.getNotesByTypeAndElementId("lead", amoCRMLead.getId());
                    List<AmoCRMNote> callsNotes = new ArrayList<>();

                    for(AmoCRMNote note : notes){
                        if(note.getNote_type().equals(10)){
                            callsNotes.add(note);
                        }
                    }

                    if(callsNotes.size() == 0){
                        System.out.println("Lead#".concat(amoCRMLead.getId().toString()).concat(" has not call notes! Adding.."));

                        // Добавляем звонок
                        AmoCRMNote amoCRMNote = new AmoCRMNote();
                        // 10 - звонок
                        amoCRMNote.setNote_type(10);
                        AmoCRMNoteText amoCRMNoteText = new AmoCRMNoteText();
                        String phone = apiPhoneNumberCustomField.getValues().get(0).getValue();
                        System.out.println("phone: " + phone);
                        amoCRMNoteText.setPhone(phone);
                        String duration = "1";
                        amoCRMNoteText.setDuration(duration);
                        String status = "4";
                        amoCRMNoteText.setCall_status(status);
                        amoCRMNoteText.setSrc("CallTracking");
                        amoCRMNote.setText(amoCRMNoteText);

                        amoCRMService.addNoteToLead(amoCRMNote, amoCRMLead);
                    }
                }
            }
        } catch (APIAuthException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPhoneNumberCustomFieldLeads(Long phoneNumberCustomFieldLeads) {
        this.phoneNumberCustomFieldLeads = phoneNumberCustomFieldLeads;
    }
}
