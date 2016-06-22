package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berz on 11.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMEntityWCustomFields extends AmoCRMEntity {
    public AmoCRMEntityWCustomFields(){}

    public void addStringValuesToCustomField(Long id, String[] values){
        addStringValuesToCustomField(id, values, null);
    }

    public void addStringValuesToCustomField(Long id, String[] values, String enumVal){
        ArrayList<AmoCRMCustomFieldValue> fieldValues = new ArrayList<>();
        for(String value : values){
            AmoCRMCustomFieldValue amoCRMCustomFieldValue = new AmoCRMCustomFieldValue(value);

            if(enumVal != null){
                amoCRMCustomFieldValue.setEnumerated(enumVal);
            }
            fieldValues.add(amoCRMCustomFieldValue);
        }

        if(this.custom_fields == null){
            this.custom_fields = new ArrayList<>();
        }

        for(AmoCRMCustomField crmCustomField : this.custom_fields){
            if(crmCustomField.getId().equals(id)){
                if(crmCustomField.getValues() == null){
                    crmCustomField.setValues(fieldValues);
                }
                else{
                    ArrayList<AmoCRMCustomFieldValue> crmCustomFieldValues = crmCustomField.getValues();

                    for(String value : values){
                        AmoCRMCustomFieldValue customFieldValue = new AmoCRMCustomFieldValue(value);

                        if(enumVal != null){
                            customFieldValue.setEnumerated(enumVal);
                        }
                        crmCustomFieldValues.add(customFieldValue);
                    }
                }

                return;
            }
        }

        AmoCRMCustomField amoCRMCustomField = new AmoCRMCustomField();
        amoCRMCustomField.setValues(fieldValues);
        amoCRMCustomField.setId(id);

        this.custom_fields.add(amoCRMCustomField);
    }

    private ArrayList<AmoCRMCustomField> custom_fields;

    public ArrayList<AmoCRMCustomField> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<AmoCRMCustomField> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public boolean checkCustomFieldEnumValue(Long fieldId, String enumValue) {

        for(AmoCRMCustomField field : this.getCustom_fields()){
            if(field.getId().equals(fieldId) && field.getValues().size() > 0){
                for(AmoCRMCustomFieldValue value : field.getValues()){
                    if(value.getEnumerated().equals(enumValue)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void setEmptyValueToField(Long fieldId) {
        for(AmoCRMCustomField crmCustomField : this.getCustom_fields()){
            if(crmCustomField.getId().equals(fieldId)){
                crmCustomField.setValues(new ArrayList<AmoCRMCustomFieldValue>());
                return;
            }
        }

        AmoCRMCustomField field = new AmoCRMCustomField();
        field.setValues(new ArrayList<AmoCRMCustomFieldValue>());
        this.getCustom_fields().add(field);
    }
}
