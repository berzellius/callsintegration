package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMCustomField {

    public AmoCRMCustomField(Long id, ArrayList<AmoCRMCustomFieldValue> values){
        this.setId(id);
        this.setValues(values);
    }

    private Long id;
    private ArrayList<AmoCRMCustomFieldValue> values;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<AmoCRMCustomFieldValue> getValues() {
        return values;
    }

    public void setValues(ArrayList<AmoCRMCustomFieldValue> values) {
        this.values = values;
    }
}
