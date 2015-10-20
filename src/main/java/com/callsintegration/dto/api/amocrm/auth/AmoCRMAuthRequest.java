package com.callsintegration.dto.api.amocrm.auth;

import com.callsintegration.dto.api.amocrm.request.AmoCRMGetRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMAuthRequest extends AmoCRMGetRequest {

    private String type;
    private String USER_LOGIN;
    private String USER_HASH;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUSER_HASH() {
        return USER_HASH;
    }

    public void setUSER_HASH(String USER_HASH) {
        this.USER_HASH = USER_HASH;
    }

    public String getUSER_LOGIN() {
        return USER_LOGIN;
    }

    public void setUSER_LOGIN(String USER_LOGIN) {
        this.USER_LOGIN = USER_LOGIN;
    }
}
