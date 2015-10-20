package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMPostRequest implements AmoCRMRequest {

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

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
