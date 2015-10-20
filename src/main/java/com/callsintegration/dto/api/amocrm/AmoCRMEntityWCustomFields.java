package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 11.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMEntityWCustomFields extends AmoCRMEntity {
    public AmoCRMEntityWCustomFields(){}

    public void addStringValuesToCustomField(Long id, String[] values){
        AmoCRMCustomField amoCRMCustomField = new AmoCRMCustomField();
        ArrayList<AmoCRMCustomFieldValue> fieldValues = new ArrayList<>();
        for(String value : values){
            AmoCRMCustomFieldValue amoCRMCustomFieldValue = new AmoCRMCustomFieldValue(value);
            fieldValues.add(amoCRMCustomFieldValue);
        }
        amoCRMCustomField.setValues(fieldValues);
        amoCRMCustomField.setId(id);

        if(this.custom_fields == null){
            this.custom_fields = new ArrayList<>();
        }
        this.custom_fields.add(amoCRMCustomField);
    }

    private ArrayList<AmoCRMCustomField> custom_fields;

    public ArrayList<AmoCRMCustomField> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<AmoCRMCustomField> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
