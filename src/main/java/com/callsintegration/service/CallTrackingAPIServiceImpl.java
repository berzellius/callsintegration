package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import com.callsintegration.dmodel.CallTrackingSourceCondition;
import com.callsintegration.dto.api.ErrorHandlers.APIRequestErrorException;
import com.callsintegration.dto.api.calltracking.*;
import com.callsintegration.exception.APIAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    @Autowired
    CallTrackingSourceConditionService callTrackingSourceConditionService;

    private static final Logger log = LoggerFactory.getLogger(CallTrackingAPIServiceImpl.class);

    private String auth;
    private Integer[] projects = {};

    private String login;
    private String password;
    private String loginURL;
    private HttpMethod loginMethod;
    private String webSiteLogin;
    private String webSitePassword;
    private String webSiteLoginUrl;
    private String webSiteLoginMethod;

    private String apiURL;
    private HttpMethod apiMethod;

    private int reLogins = 0;
    private int reLoginsMax = 3;

    private String metrics = "ct:duration,ct:answer_time";
    private String dimensions = "ct:caller,ct:datetime,ct:source,ct:status";

    private ResponseErrorHandler errorHandler;

    private List<String> cookies;


    public Integer[] getProjects() {
        return projects;
    }

    private MultiValueMap<String, String> createWebSiteLoginParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", this.getWebSiteLogin());
        params.add("password", this.getWebSitePassword());

        return params;
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
        if(this.cookies != null) {
            requestHeaders.add("Cookie", String.join(";", this.cookies));
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, requestHeaders);
        return  request;
    }

    @Override
    public void setProjects(Integer[] projects) {
        this.projects = projects;
    }

    private RestTemplate getCallTrackingWebsiteRestTemplate(){
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

    private RestTemplate getCallTrackingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(this.errorHandler);

        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        FormHttpMessageConverter httpMessageConverter = new FormHttpMessageConverter();

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.APPLICATION_JSON);

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

    private boolean checkWebsiteLoggedIn(){
        if(this.cookies == null)
            return false;

        for(String cookie : this.cookies){
            String[] strings = cookie.split("=");
            if(strings.length > 0 && strings[0].equals("I-CMS_AUTH")){
                return true;
            }
        }

        return false;
    }

    /*
    *
    * Обновить данные по рекламным каналам
     */
    @Override
    public void updateMarketingChannelsFromCalltracking() throws APIAuthException {
        callTrackingSourceConditionService.updateSources(this.getAllMarketingChannelsFromCalltracking());
    }

    @Override
    public List<CallTrackingSourceCondition> getAllMarketingChannelsFromCalltracking() throws APIAuthException {
        websiteLogIn();

        List<CallTrackingSourceCondition> callTrackingSourceConditions = new LinkedList<>();

        for(Integer projectId : this.getProjects()){
            getAllMarketingChannelsFromCalltrackingByProjectId(projectId, callTrackingSourceConditions);
        }

        return callTrackingSourceConditions;
    }

    private void getAllMarketingChannelsFromCalltrackingByProjectId(Integer projectId, List<CallTrackingSourceCondition> listToPush) throws APIAuthException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sidx", "id");
        params.add("sord", "desc");

        HttpEntity<MultiValueMap<String, String>> request = this
                .requestByParams(params);

        CallTrackingWebsiteSources callTrackingWebsiteSources = this
                .request("https://calltracking.ru/admin/" + projectId + "/sources_manage", HttpMethod.POST, request, CallTrackingWebsiteSources.class);

        for(CallTrackingWebsiteSource callTrackingWebsiteSource : callTrackingWebsiteSources.getRows()){
            MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
            params1.add("id", callTrackingWebsiteSource.getSourceId().toString());
            params1.add("action", "get_source_info");

            HttpEntity<MultiValueMap<String, String>> request1 = this
                    .requestByParams(params1);

            CallTrackingWebsiteSourceConditionsResponse callTrackingWebsiteSourceConditionsResponse = this
                    .request("https://calltracking.ru/admin/" + projectId + "/sources_manage/view/actions", HttpMethod.POST, request1, CallTrackingWebsiteSourceConditionsResponse.class);

            if(callTrackingWebsiteSourceConditionsResponse.getResponse() == null || callTrackingWebsiteSourceConditionsResponse.getResponse().getConditions() == null){
                return;
            }

            for(CallTrackingWebsiteSourceCondition callTrackingWebsiteSourceCondition : callTrackingWebsiteSourceConditionsResponse.getResponse().getConditions()) {
                String[] utmSources = callTrackingWebsiteSourceCondition.getUtm_source().split(",");
                String[] utmMediums = callTrackingWebsiteSourceCondition.getUtm_medium().split(",");
                String[] utmCampaigns = callTrackingWebsiteSourceCondition.getUtm_campaign().split(",");

                for(String utmSource : utmSources){
                    for(String utmMedium : utmMediums){
                        for(String utmCampaign : utmCampaigns){
                            int truth = 0;
                            if(!utmSource.equals(""))
                                truth++;
                            if(!utmMedium.equals(""))
                                truth++;
                            if(!utmCampaign.equals(""))
                                truth++;

                            CallTrackingSourceCondition callTrackingSourceCondition = new CallTrackingSourceCondition();
                            callTrackingSourceCondition.setSourceName(callTrackingWebsiteSource.getSourceName());
                            callTrackingSourceCondition.setUtmSource(utmSource);
                            callTrackingSourceCondition.setUtmMedium(utmMedium);
                            callTrackingSourceCondition.setUtmCampaign(utmCampaign);
                            callTrackingSourceCondition.setProjectId(projectId);
                            callTrackingSourceCondition.setTruth(truth);
                            callTrackingSourceCondition.setPhonesCount(callTrackingWebsiteSource.getPhones_count());

                            listToPush.add(callTrackingSourceCondition);
                        }
                    }
                }
            }

        }
    }

    private void websiteLogIn() throws APIAuthException {
        /*this.reLogins = 0;

        while (!this.checkWebsiteLoggedIn()) {
            System.out.println("loggin in..");
            if(this.reLogins > this.reLoginsMax){
                throw new APIAuthException("cant login to calltracking website");
            }

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(this.getWebSiteLoginUrl());
            post.setHeader("User-Agent", "Mozilla/5.0");
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("email", this.getWebSiteLogin()));
            urlParameters.add(new BasicNameValuePair("password", this.getWebSitePassword()));

            try {
                this.cookies = new LinkedList<>();
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                HttpResponse response = client.execute(post);

                Header[] headers = response.getAllHeaders();

                for (Header header : headers) {
                    if (header.getName().equals("Set-Cookie")) {
                        this.cookies.add(header.getValue());
                    }
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("encoding not supported!");
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.reLogins++;
        }*/
        this.reLogins = 0;

        while (!this.checkWebsiteLoggedIn()) {
            System.out.println("loggin in..");
            if(this.reLogins > this.reLoginsMax){
                throw new APIAuthException("cant login to calltracking website");
            }
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("login", this.getWebSiteLogin());
            params.add("password", this.getWebSitePassword());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, requestHeaders);
            this.cookies = new LinkedList<>();
            HttpEntity<String> response = restTemplate.exchange(this.getWebSiteLoginUrl(), HttpMethod.POST, request, String.class);
            HttpHeaders httpHeaders = response.getHeaders();
            if(httpHeaders.containsKey("Set-Cookie")){
                for(String cookie : httpHeaders.get("Set-Cookie")){
                    this.cookies.add(cookie.split(";")[0]);
                }
            }

            this.reLogins++;
        }
    }

    private <T> T request(String url, HttpMethod method, HttpEntity<MultiValueMap<String, String>> request, Class<T> cl) throws APIAuthException {
        return request(url, method, request, cl, APIRequestErrorException.class);
    }

    private <T, TE> T request(String url, HttpMethod method, HttpEntity<MultiValueMap<String, String>> request, Class<T> cl, Class<TE> exceptionType) throws APIAuthException {
        return request(url, method, request, this.getCallTrackingRestTemplate(), cl, exceptionType);
    }

    private <T, TE> T request(String url, HttpMethod method, HttpEntity<MultiValueMap<String, String>> request, RestTemplate rt, Class<T> cl, Class<TE> exceptionType) throws APIAuthException {

        try {
            HttpEntity<T> response = this.getCallTrackingRestTemplate().exchange(
                    url, method, request, cl
            );

            return (T) response.getBody();
        }
        catch (Exception e){
            if(exceptionType.isInstance(e)) {
                System.out.println("request to calltracking failed with error: " + e.toString());
                return null;
            }
            else{
                throw e;
            }
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
            Long startIndex = callsService.callsAlreadyLoaded(project, from, to);
            log.info("for project #" + project + " we have " + startIndex + " records");
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

    @Override
    public String getWebSiteLogin() {
        return webSiteLogin;
    }

    @Override
    public void setWebSiteLogin(String webSiteLogin) {
        this.webSiteLogin = webSiteLogin;
    }

    @Override
    public String getWebSitePassword() {
        return webSitePassword;
    }

    @Override
    public void setWebSitePassword(String webSitePassword) {
        this.webSitePassword = webSitePassword;
    }

    @Override
    public String getWebSiteLoginUrl() {
        return webSiteLoginUrl;
    }

    @Override
    public void setWebSiteLoginUrl(String webSiteLoginUrl) {
        this.webSiteLoginUrl = webSiteLoginUrl;
    }

    @Override
    public String getWebSiteLoginMethod() {
        return webSiteLoginMethod;
    }

    @Override
    public void setWebSiteLoginMethod(String webSiteLoginMethod) {
        this.webSiteLoginMethod = webSiteLoginMethod;
    }

    @Override
    public void processCallOnImport(Call call) {
        log.info("processing call on import..");
        call.setDtmCreate(new Date());

        if(call.getNumber() != null){
            if(call.getNumber().length() >= 11 &&
                    (
                            call.getNumber().charAt(0) == '7'
                    )){
                call.setNumber(call.getNumber().substring(1));
            }
        }
    }


}
