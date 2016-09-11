package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import com.callsintegration.dmodel.CallTrackingSourceCondition;
import com.callsintegration.dto.api.calltracking.CallTrackingWebsiteSources;
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

    /*
    *
    * Обновить данные по рекламным каналам
     */
    void updateMarketingChannelsFromCalltracking() throws APIAuthException;

    List<CallTrackingSourceCondition> getAllMarketingChannelsFromCalltracking() throws APIAuthException;

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

    String getWebSiteLogin();

    void setWebSiteLogin(String webSiteLogin);

    String getWebSitePassword();

    void setWebSitePassword(String webSitePassword);

    String getWebSiteLoginUrl();

    void setWebSiteLoginUrl(String webSiteLoginUrl);

    String getWebSiteLoginMethod();

    void setWebSiteLoginMethod(String webSiteLoginMethod);

    void processCallOnImport(Call call);
}
