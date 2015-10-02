package com.callsintegration.service;

import org.springframework.stereotype.Service;

/**
 * Created by berz on 02.10.2015.
 */
@Service
public class AmoCRMServiceImpl implements AmoCRMService {

    private String userLogin;
    private String userHash;
    private String loginUrl;
    private String apiBaseUrl;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }
}
