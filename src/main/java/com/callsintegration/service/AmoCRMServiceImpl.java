package com.callsintegration.service;

import com.callsintegration.dto.api.ErrorHandlers.APIRequestErrorException;
import com.callsintegration.dto.api.amocrm.*;
import com.callsintegration.dto.api.amocrm.auth.AmoCRMAuthResponse;
import com.callsintegration.dto.api.amocrm.request.*;
import com.callsintegration.dto.api.amocrm.response.*;
import com.callsintegration.exception.APIAuthException;
import com.callsintegration.settings.ProjectSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by berz on 02.10.2015.
 */
@Service
public class AmoCRMServiceImpl implements AmoCRMService {

    @Autowired
    ProjectSettings projectSettings;

    private String userLogin;
    private String userHash;
    private String loginUrl;
    private String apiBaseUrl;
    private ArrayList<Long> leadClosedStatusesIDs = new ArrayList<>();


    private List<String> cookies;

    private ResponseErrorHandler errorHandler;

    private Integer maxRelogins;
    private Integer relogins = 0;


    @Override
    public void logIn() throws APIAuthException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("USER_LOGIN", this.getUserLogin());
        params.add("USER_HASH", this.getUserHash());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, requestHeaders);
        HttpEntity<AmoCRMAuthResponse> response = restTemplate.exchange(this.getLoginUrl(), HttpMethod.POST, request, AmoCRMAuthResponse.class);

        if (response.getBody().getResponse().auth) {
            if (response.getHeaders().containsKey("Set-Cookie")) {
                this.cookies = response.getHeaders().get("Set-Cookie");
            }
        } else throw new APIAuthException("cant authentificate to AmoCRM!");
    }

    private HttpEntity<MultiValueMap<String, String>> plainHttpEntity() throws APIAuthException {
        if (cookies == null) {
            this.logIn();
        }

        //MultiValueMap<String, String> params = mapFromRequest(amoCRMRequest);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", String.join(";", this.cookies));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, requestHeaders);

        return request;
    }

    private HttpEntity<AmoCRMRequest> jsonHttpEntity(AmoCRMRequest req) throws APIAuthException {

        if (cookies == null) {
            this.logIn();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Cookie", String.join(";", this.cookies));


        HttpEntity<AmoCRMRequest> requestHttpEntity = new HttpEntity<>(req, httpHeaders);

        return requestHttpEntity;
    }

    @Override
    public AmoCRMCreatedEntityResponse addContact(AmoCRMContact amoCRMContact) throws APIAuthException {
        AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
        ArrayList<AmoCRMContact> amoCRMContacts = new ArrayList<>();

        amoCRMContacts.add(amoCRMContact);
        amoCRMEntities.setAdd(amoCRMContacts);
        amoCRMEntities.setUpdate(new ArrayList<AmoCRMContact>());

        AmoCRMCreatedContactsResponse amoCRMCreatedContactsResponse = this.editContacts(amoCRMEntities);

        if (
                amoCRMCreatedContactsResponse.getResponse() == null ||
                        amoCRMCreatedContactsResponse.getResponse().getContacts().getAdd().size() == 0
                ) {
            throw new IllegalStateException("Контакт не создан! Пришел пустой ответ от AmoCRM API.");
        }

        return amoCRMCreatedContactsResponse.getResponse().getContacts().getAdd().get(0);
    }

    @Override
    public AmoCRMCreatedContactsResponse editContacts(AmoCRMEntities amoCRMEntities) throws APIAuthException {
        if(projectSettings.amoCRMReadOnlyMode()){
            return null;
        }

        AmoCRMContactPostRequest amoCRMContactPostRequest = new AmoCRMContactPostRequest();
        amoCRMContactPostRequest.setContacts(amoCRMEntities);

        AmoCRMPostRequest amoCRMJsonRequest = new AmoCRMPostRequest(amoCRMContactPostRequest);

        HttpEntity<AmoCRMCreatedContactsResponse> amoCRMCreatedContactsResponse = request(amoCRMJsonRequest, "contacts/set", AmoCRMCreatedContactsResponse.class);
        if (amoCRMCreatedContactsResponse.getBody() == null) {
            throw new IllegalStateException("Ошибка в ходе обновления контактов! Пришел пустой ответ от AmoCRM API.");
        }

        return amoCRMCreatedContactsResponse.getBody();
    }

    @Override
    public void addContactToLead(AmoCRMContact amoCRMContact, AmoCRMLead amoCRMLead) throws APIAuthException {
        ArrayList<AmoCRMContact> amoCRMContacts = new ArrayList<>();
        amoCRMContacts.add(amoCRMContact);

        this.addContactsToLead(amoCRMContacts, amoCRMLead);
    }

    @Override
    public AmoCRMCreatedNotesResponse addNoteToLead(AmoCRMNote amoCRMNote, AmoCRMLead amoCRMLead) throws APIAuthException {
        if(projectSettings.amoCRMReadOnlyMode()){
            return null;
        }

        if (amoCRMLead.getId() == null) {
            throw new IllegalArgumentException("Передана не сохраненная в системе сделка!");
        }

        amoCRMNote.setElement_id(amoCRMLead.getId());
        amoCRMNote.setElement_type(2);

        AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
        ArrayList<AmoCRMNote> amoCRMNotes = new ArrayList<>();
        amoCRMNotes.add(amoCRMNote);
        amoCRMEntities.setAdd(amoCRMNotes);

        AmoCRMNotesPostRequest amoCRMNotesPostRequest = new AmoCRMNotesPostRequest(amoCRMEntities);
        AmoCRMPostRequest amoCRMPostRequest = new AmoCRMPostRequest(amoCRMNotesPostRequest);

        HttpEntity<AmoCRMCreatedNotesResponse> amoCRMCreatedNotesResponseHttpEntity = request(amoCRMPostRequest, "notes/set", AmoCRMCreatedNotesResponse.class);

        if (amoCRMCreatedNotesResponseHttpEntity.getBody() == null) {
            throw new IllegalStateException("Ошибка при добавлении заметок! Пустой ответ от  AmoCRM API");
        }

        return amoCRMCreatedNotesResponseHttpEntity.getBody();
    }

    @Override
    public void addContactsToLead(ArrayList<AmoCRMContact> amoCRMContacts, AmoCRMLead amoCRMLead) throws APIAuthException {
        for (AmoCRMContact amoCRMContact : amoCRMContacts) {
            amoCRMContact.addLinkedLeadById(amoCRMLead.getId());
        }

        AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
        amoCRMEntities.setUpdate(amoCRMContacts);

        AmoCRMCreatedContactsResponse amoCRMCreatedContactsResponse = this.editContacts(amoCRMEntities);

        if (
                amoCRMCreatedContactsResponse.getResponse() == null
                ) {
            throw new IllegalStateException(
                    "Ошибка при добавлении контактов к сделке. Пустой ответ от AmoCRM! Response: " +
                            amoCRMCreatedContactsResponse.toString()
            );
        }
    }

    @Override
    public List<AmoCRMContactsLeadsLink> getContactsLeadsLinksByContact(AmoCRMContact amoCRMContact) throws APIAuthException {
        AmoCRMContactsLeadsLinksRequest amoCRMContactsLeadsLinksRequest = new AmoCRMContactsLeadsLinksRequest();
        amoCRMContactsLeadsLinksRequest.setContacts_link(amoCRMContact.getId());

        HttpEntity<AmoCRMContactsLeadsLinksResponse> response = request(
                amoCRMContactsLeadsLinksRequest, "contacts/links", AmoCRMContactsLeadsLinksResponse.class
        );

        AmoCRMContactsLeadsLinksResponse amoCRMContactsLeadsLinksResponse = response.getBody();

        if (amoCRMContactsLeadsLinksResponse == null || amoCRMContactsLeadsLinksResponse.getResponse() == null) {
            return new LinkedList<>();
        }

        return amoCRMContactsLeadsLinksResponse.getResponse().getLinks();
    }

    public List<AmoCRMContact> getContactsByQuery(String query, Long limit, Long offset) throws APIAuthException {
        AmoCRMContactsGetRequest amoCRMContactsGetRequest = new AmoCRMContactsGetRequest();
        amoCRMContactsGetRequest.setQuery(query);
        amoCRMContactsGetRequest.setLimit_rows(limit);
        amoCRMContactsGetRequest.setLimit_offset(offset);

        HttpEntity<AmoCRMContactsResponse> response = request(
                amoCRMContactsGetRequest, "contacts/list", AmoCRMContactsResponse.class);

        AmoCRMContactsResponse amoCRMContactsResponse = response.getBody();

        if (amoCRMContactsResponse == null || amoCRMContactsResponse.getResponse() == null) {
            return new LinkedList<>();
        }

        return amoCRMContactsResponse.getResponse().getContacts();
    }

    @Override
    public List<AmoCRMContact> getContactsByQuery(String query) throws APIAuthException {
        return this.getContactsByQuery(query, 500l, 0l);
    }

    @Override
    public List<AmoCRMLead> getLeadsByQuery(String query) throws APIAuthException {
        return this.getLeadsByQuery(query, 500l, 0l);
    }

    @Override
    public List<AmoCRMLead> getLeadsByQuery(String query, Long limit, Long offset) throws APIAuthException {
        AmoCRMLeadsGetRequest amoCRMLeadsGetRequest = new AmoCRMLeadsGetRequest();
        amoCRMLeadsGetRequest.setQuery(query);
        amoCRMLeadsGetRequest.setLimit_rows(limit);
        amoCRMLeadsGetRequest.setLimit_offset(offset);

        HttpEntity<AmoCRMLeadsResponse> response = request(
                amoCRMLeadsGetRequest, "leads/list", AmoCRMLeadsResponse.class
        );

        AmoCRMLeadsResponse amoCRMLeadsResponse = response.getBody();

        if (amoCRMLeadsResponse == null || amoCRMLeadsResponse.getResponse() == null) {
            return new ArrayList<>();
        }

        return amoCRMLeadsResponse.getResponse().getLeads();
    }

    @Override
    public List<AmoCRMNote> getNotesByTypeAndElementId(String type, Long elementId) throws APIAuthException {
        AmoCRMNotesGetRequest amoCRMNotesGetRequest = new AmoCRMNotesGetRequest();
        amoCRMNotesGetRequest.setElement_id(elementId);
        amoCRMNotesGetRequest.setType(type);

        HttpEntity<AmoCRMNotesResponse> response = request(
                amoCRMNotesGetRequest, "notes/list", AmoCRMNotesResponse.class
        );

        AmoCRMNotesResponse amoCRMNotesResponse = response.getBody();

        if(amoCRMNotesResponse == null || amoCRMNotesResponse.getResponse() == null){
            return new ArrayList<>();
        }

        return amoCRMNotesResponse.getResponse().getNotes();
    }

    @Override
    public AmoCRMLead getLeadById(Long leadId) throws APIAuthException {
        AmoCRMLeadsGetRequest amoCRMLeadsGetRequest = new AmoCRMLeadsGetRequest();
        amoCRMLeadsGetRequest.setId(leadId);
        amoCRMLeadsGetRequest.setLimit_rows(1l);
        amoCRMLeadsGetRequest.setLimit_offset(0l);

        HttpEntity<AmoCRMLeadsResponse> response = request(
                amoCRMLeadsGetRequest, "leads/list", AmoCRMLeadsResponse.class
        );

        AmoCRMLeadsResponse amoCRMLeadsResponse = response.getBody();

        if (amoCRMLeadsResponse == null || amoCRMLeadsResponse.getResponse() == null) {
            return null;
        }

        return amoCRMLeadsResponse.getResponse().getLeads().get(0);
    }

    @Override
    public AmoCRMCreatedEntityResponse addLead(AmoCRMLead amoCRMLead) throws APIAuthException {
        AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
        ArrayList<AmoCRMLead> amoCRMLeads = new ArrayList<>();
        amoCRMLeads.add(amoCRMLead);

        amoCRMEntities.setAdd(amoCRMLeads);
        AmoCRMCreatedLeadsResponse amoCRMCreatedLeadsResponse = this.editLeads(amoCRMEntities);

        if (amoCRMCreatedLeadsResponse == null ||
                amoCRMCreatedLeadsResponse.getResponse() == null ||
                amoCRMCreatedLeadsResponse.getResponse().getLeads().getAdd().size() == 0
                ) {
            throw new IllegalStateException("Ошибка при создании сделки! Пришел пустой ответ от AmoCRM API!");
        }

        return amoCRMCreatedLeadsResponse.getResponse().getLeads().getAdd().get(0);
    }

    private void updateLast_modified(AmoCRMEntities amoCRMEntities){
        for(AmoCRMEntity amoCRMEntity : amoCRMEntities.getUpdate()){
            // Увеличиваем last_modified, иначе изменения не будут учтены
            amoCRMEntity.setLast_modified(amoCRMEntity.getLast_modified() + 1);
        }
    }

    @Override
    public AmoCRMCreatedLeadsResponse editLeads(AmoCRMEntities amoCRMEntities) throws APIAuthException {

        if(projectSettings.amoCRMReadOnlyMode()){
            return null;
        }

        updateLast_modified(amoCRMEntities);

        AmoCRMLeadPostRequest amoCRMLeadPostRequest = new AmoCRMLeadPostRequest(amoCRMEntities);
        AmoCRMPostRequest amoCRMPostRequest = new AmoCRMPostRequest(amoCRMLeadPostRequest);

        HttpEntity<AmoCRMCreatedLeadsResponse> amoCRMCreatedEntityResponse =
                request(amoCRMPostRequest, "leads/set", AmoCRMCreatedLeadsResponse.class);

        if (amoCRMCreatedEntityResponse == null || amoCRMCreatedEntityResponse.getBody() == null) {
            throw new IllegalStateException("Ошибка при обновлении сделок! Пришел пустой ответ от AmoCRM API");
        }

        return amoCRMCreatedEntityResponse.getBody();
    }

    private void request(AmoCRMRequest amoCRMRequest, String url) throws APIAuthException {
        request(amoCRMRequest, url, String.class);
    }


    private List<Field> getFields(Class<? extends AmoCRMRequest> cl) {
        List<Field> f = new ArrayList<Field>();
        f.addAll(Arrays.asList(cl.getDeclaredFields()));

        Class s = cl.getSuperclass();
        if (s != null) {
            List<Field> sf = getFields(s);
            f.addAll(sf);
        }

        return f;
    }

    private UriComponentsBuilder uriComponentsBuilderByParams(AmoCRMRequest amoCRMRequest, String url) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);

        List<Field> fields = this.getFields(amoCRMRequest.getClass());
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                if (f.get(amoCRMRequest) != null) {
                    uriComponentsBuilder
                            .queryParam(f.getName(), f.get(amoCRMRequest).toString());
                    //params.add(f.getName(), f.get(amoCRMRequest).toString());
                }
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
            }
        }

        return uriComponentsBuilder;
    }

    private <T> HttpEntity<T> request(AmoCRMRequest amoCRMRequest, String url, Class<T> cl) throws APIAuthException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(this.errorHandler);


        HttpEntity<T> response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (amoCRMRequest.getHttpMethod().equals(HttpMethod.GET)) {
                HttpEntity<MultiValueMap<String, String>> requestHttpEntity = plainHttpEntity();

                UriComponentsBuilder uriComponentsBuilder = this.uriComponentsBuilderByParams(
                        amoCRMRequest, this.getApiBaseUrl().concat(url)
                );

                response = restTemplate.exchange(
                        uriComponentsBuilder.build().encode().toUri(),
                        amoCRMRequest.getHttpMethod(), requestHttpEntity, cl
                );
            } else {
                HttpEntity<AmoCRMRequest> requestHttpEntity = jsonHttpEntity(amoCRMRequest);


                try {
                    System.out.println(objectMapper.writeValueAsString(requestHttpEntity.getBody()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    System.out.println("Cant present response as JSON object!");
                }

                response = restTemplate.exchange(
                        this.getApiBaseUrl().concat(url),
                        amoCRMRequest.getHttpMethod(), requestHttpEntity, cl
                );
            }

            this.relogins = 0;

            return response;
        } catch (APIRequestErrorException e) {
            System.out.println("request to amocrm failed with error: " + e.getParams().toString());

            if (e.getParams().get("code") != null && e.getParams().get("code").equals("401")) {
                if (this.relogins < this.maxRelogins) {
                    this.logIn();
                    this.relogins++;
                    System.out.println("Login to AmoCRM!");
                    return request(amoCRMRequest, url, cl);
                } else {
                    System.out.println("Maximum relogins count (" + this.maxRelogins + ") reached for AmoCRM!");
                    return null;
                }
            } else return null;
        } catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUserLogin() {
        return userLogin;
    }

    @Override
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String getUserHash() {
        return userHash;
    }


    @Override
    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    @Override
    public String getLoginUrl() {
        return loginUrl;
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    @Override
    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    @Override
    public ArrayList<Long> getLeadClosedStatusesIDs() {
        return leadClosedStatusesIDs;
    }

    @Override
    public void setLeadClosedStatusesIDs(ArrayList<Long> leadClosedStatusesIDs) {
        this.leadClosedStatusesIDs = leadClosedStatusesIDs;
    }

    @Override
    public void setErrorHandler(ResponseErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void setMaxRelogins(Integer maxRelogins) {
        this.maxRelogins = maxRelogins;
    }
}
