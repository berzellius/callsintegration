package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.ToString;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = AmoCRMCustomFieldValueSerializer.class)
@JsonDeserialize(using = AmoCRMCustomFieldValueDeserializer.class)
public class AmoCRMCustomFieldValue {

    public AmoCRMCustomFieldValue(){}

    public AmoCRMCustomFieldValue(String value){
        this.setValue(value);
    }

    private String value;

    private String enumerated;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEnumerated() {
        return enumerated;
    }

    public void setEnumerated(String enumerated) {
        this.enumerated = enumerated;
    }
}
