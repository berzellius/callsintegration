package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 06.11.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMNotesGetRequest extends AmoCRMQueryGetRequest {
    public AmoCRMNotesGetRequest(){}

    private String type;

    private Long element_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getElement_id() {
        return element_id;
    }

    public void setElement_id(Long element_id) {
        this.element_id = element_id;
    }
}
