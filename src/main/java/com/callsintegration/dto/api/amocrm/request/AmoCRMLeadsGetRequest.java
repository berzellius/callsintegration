package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * Created by berz on 07.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMLeadsGetRequest extends AmoCRMQueryGetRequest {
    public AmoCRMLeadsGetRequest(){}

    private Long status;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
