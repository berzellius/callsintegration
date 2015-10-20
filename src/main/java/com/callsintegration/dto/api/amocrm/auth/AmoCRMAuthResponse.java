package com.callsintegration.dto.api.amocrm.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.security.Timestamp;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMAuthResponse {

    public AuthData getResponse() {
        return response;
    }

    public void setResponse(AuthData response) {
        this.response = response;
    }

    public class AuthData{
        public Boolean auth;
        public Integer server_time;
    }

    private AuthData response;


}
