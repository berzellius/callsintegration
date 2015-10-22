package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import com.callsintegration.dto.api.ErrorHandlers.APIRequestErrorException;
import com.callsintegration.dto.api.calltracking.CallTrackingAuth;
import com.callsintegration.dto.api.calltracking.CallTrackingData;
import com.callsintegration.dto.api.calltracking.CallTrackingResponse;
import com.callsintegration.exception.APIAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berz on 22.09.2015.
 */
@Service
public class CallTrackingAPIServiceImpl implements CallTrackingAPIService {

    @Autowired
    CallsService callsService;

    private String auth;
    private Integer[] projects = {};
    private String login;
    private String password;
    private String loginURL;
    private HttpMethod loginMethod;
    private String apiURL;
    private HttpMethod apiMethod;

    private int reLogins = 0;
    private int reLoginsMax = 3;

    private String metrics = "ct:duration,ct:answer_time";
    private String dimensions = "ct:caller,ct:datetime,ct:source,ct:status";

    private ResponseErrorHandler errorHandler;


    public Integer[] getProjects() {
        return projects;
    }

    private MultiValueMap<String, String> createLoginParams(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("account_type", "calltracking");
        params.add("login", this.getLogin());
        params.add("password", this.getPassword());
        params.add("service","analytics");

        return params;
    }

    private HttpEntity<MultiValueMap<String, String>> requestByParams(MultiValueMap<String, String> params){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, requestHeaders);
        return  request;
    }

    @Override
    public void setProjects(Integer[] projects) {
        this.projects = projects;
    }

    private RestTemplate getCallTrackingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(this.errorHandler);

        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        FormHttpMessageConverter httpMessageConverter = new FormHttpMessageConverter();

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.TEXT_HTML);

        List<MediaType> formsMediaTypes = new ArrayList<MediaType>();
        formsMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        jsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        httpMessageConverter.setSupportedMediaTypes(formsMediaTypes);

        messageConverters.add(jsonHttpMessageConverter);
        messageConverters.add(httpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    private void logIn() throws APIAuthException {

        HttpEntity<MultiValueMap<String, String>> request = this
                .requestByParams(this.createLoginParams());

        CallTrackingAuth callTrackingAuth = (CallTrackingAuth) this
                .request(
                        this.getLoginURL(), this.getLoginMethod(), request, CallTrackingAuth.class
                );

        if(
                !(callTrackingAuth.getError_code().equals(0) &&
                        callTrackingAuth.getStatus().equals("ok"))
                ){
            throw new APIAuthException("Cant authentificate!");
        }

        this.auth = callTrackingAuth.getData();
        this.reLogins = 0;
    }


    private <T> CallTrackingResponse request(String url, HttpMethod method, HttpEntity<MultiValueMap<String, String>> request, Class<T> cl) throws APIAuthException {

        try {
            HttpEntity<T> response = this.getCallTrackingRestTemplate().exchange(
                    url, method, request, cl
            );

            return (CallTrackingResponse) response.getBody();
        }
        catch (APIRequestErrorException e){
            System.out.println("request to calltracking failed with error: " + e.getParams().toString());
            return null;
        }
    }

    private MultiValueMap<String, String> apiParams(Date from, Date to, Long startIndex, Integer maxResults){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dimensions", this.dimensions);
        params.add("metrics", this.metrics);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        params.add("start-date", dt.format(from));
        params.add("end-date", dt.format(to));
        params.add("sort", "ct:datetime");

        if(startIndex != null){
            params.add("start-index", startIndex.toString());
        }

        if(maxResults != null){
            params.add("max-results", maxResults.toString());
        }

        return params;
    }


    @Override
    public List<Call> getCalls(Date from, Date to, Integer maxResults) throws APIAuthException {
        List<Call> calls = new LinkedList<>();

        for(Integer project : this.projects){
            Long startIndex = callsService.callsAlreadyLoaded(project);
            MultiValueMap<String, String> params = apiParams(from, to, startIndex, maxResults);
            params.add("project", project.toString());

            CallTrackingData callTrackingData = this.getData(params);
            for(Call call : callTrackingData.getData()){
                call.setState(Call.State.NEW);
                call.setProjectId(project);
                calls.add(call);
            }
        }
        return calls;
    }

    @Override
    public List<Call> getCalls(Date from, Date to) throws APIAuthException {
        return getCalls(from, to, null);
    }

    private CallTrackingData getData(MultiValueMap<String, String> params) throws APIAuthException {
        if(this.auth == null){
            this.logIn();
        }

        params.add("auth", this.auth);

        HttpEntity<MultiValueMap<String, String>> req = this
                .requestByParams(params);

        CallTrackingData callTrackingData = (CallTrackingData) this
                .request(
                        this.getApiURL(), this.getApiMethod(), req, CallTrackingData.class
                );

        if(callTrackingData.getError_code().equals(4)){
            if(this.reLogins >= this.reLoginsMax){
                throw new APIAuthException("Cant authentificate after " + this.reLogins + " times");
            }

            this.reLogins++;

            this.logIn();
            return this.getData(params);
        }
        else{
            this.reLogins = 0;

            if(
                    !(callTrackingData.getError_code().equals(0) &&
                            callTrackingData.getStatus().equals("ok"))
                    ){
                throw new IllegalStateException("Error: " + callTrackingData.getError_code() + "( " + callTrackingData.getError_text() + " )");
            }

            return callTrackingData;
        }
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getLoginURL() {
        return loginURL;
    }

    @Override
    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    @Override
    public HttpMethod getLoginMethod() {
        return loginMethod;
    }

    @Override
    public void setLoginMethod(HttpMethod loginMethod) {
        this.loginMethod = loginMethod;
    }

    @Override
    public String getApiURL() {
        return apiURL;
    }

    @Override
    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    @Override
    public HttpMethod getApiMethod() {
        return apiMethod;
    }

    @Override
    public void setApiMethod(HttpMethod apiMethod) {
        this.apiMethod = apiMethod;
    }

    @Override
    public void setErrorHandler(ResponseErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
