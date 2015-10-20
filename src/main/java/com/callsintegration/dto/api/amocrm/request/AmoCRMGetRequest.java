package com.callsintegration.dto.api.amocrm.request;

import org.springframework.http.HttpMethod;

/**
 * Created by berz on 02.10.2015.
 */
public class AmoCRMGetRequest implements AmoCRMRequest {
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
