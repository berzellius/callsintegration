package com.callsintegration.dto.api.amocrm.auth;

import com.callsintegration.dto.api.amocrm.AmoCRMAccount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.security.Timestamp;
import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMAuthResponse {

    public AuthData getResponse() {
        return response;
    }

    public void setResponse(AuthData response) {
        this.response = response;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class AuthData{
        public Boolean auth;
        public ArrayList<AmoCRMAccount> accounts;
        public Integer server_time;
    }

    private AuthData response;


}
