package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * Created by berz on 02.10.2015.
 */
public interface AmoCRMRequest {

    public HttpMethod getHttpMethod();

}
