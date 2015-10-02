package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMPostRequest {

    public AmoCRMPostRequest(AmoCRMEntityPostRequest request){
        this.request = request;
    }

    private AmoCRMEntityPostRequest request;

    public AmoCRMEntityPostRequest getRequest() {
        return request;
    }

    public void setRequest(AmoCRMEntityPostRequest request) {
        this.request = request;
    }
}
