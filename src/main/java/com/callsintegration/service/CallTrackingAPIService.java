package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import com.callsintegration.exception.APIAuthException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;

import java.util.Date;
import java.util.List;

/**
 * Created by berz on 22.09.2015.
 */
@Service
public interface CallTrackingAPIService {
    void setProjects(Integer[] projects);

    List<Call> getCalls(Date from, Date to, Integer maxResults) throws APIAuthException;

    List<Call> getCalls(Date from, Date to) throws APIAuthException;

    String getLogin();

    void setLogin(String login);

    String getPassword();

    void setPassword(String password);

    String getLoginURL();

    void setLoginURL(String loginURL);

    HttpMethod getLoginMethod();

    void setLoginMethod(HttpMethod loginMethod);

    String getApiURL();

    void setApiURL(String apiURL);

    HttpMethod getApiMethod();

    void setApiMethod(HttpMethod apiMethod);

    void setErrorHandler(ResponseErrorHandler errorHandler);
}
