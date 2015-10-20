package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 04.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContactsGetRequest extends AmoCRMQueryGetRequest {
    public AmoCRMContactsGetRequest(){}

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
