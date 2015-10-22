package com.callsintegration.dto.api.ErrorHandlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by berz on 22.10.2015.
 */
public class APIRequestErrorException extends RuntimeException {

    private Map<String, Object> params;

    public APIRequestErrorException(String msg){
        super(msg);
    }

    public APIRequestErrorException(Map<String, Object> properties){
        super();
        this.setParams(properties);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
