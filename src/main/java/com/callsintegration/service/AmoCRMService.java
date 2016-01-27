package com.callsintegration.service;

import com.callsintegration.dto.api.amocrm.*;
import com.callsintegration.dto.api.amocrm.response.*;
import com.callsintegration.exception.APIAuthException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berz on 02.10.2015.
 */
@Service
public interface AmoCRMService {
    void logIn() throws APIAuthException;

    AmoCRMCreatedEntityResponse addContact(AmoCRMContact amoCRMContact) throws APIAuthException;

    AmoCRMCreatedContactsResponse editContacts(AmoCRMEntities amoCRMEntities) throws APIAuthException;

    void addContactToLead(AmoCRMContact amoCRMContact, AmoCRMLead amoCRMLead) throws APIAuthException;

    AmoCRMCreatedNotesResponse addNoteToLead(AmoCRMNote amoCRMNote, AmoCRMLead amoCRMLead) throws APIAuthException;

    void addContactsToLead(ArrayList<AmoCRMContact> amoCRMContacts, AmoCRMLead amoCRMLead) throws APIAuthException;

    List<AmoCRMContactsLeadsLink> getContactsLeadsLinksByContact(AmoCRMContact amoCRMContact) throws APIAuthException;

    List<AmoCRMContact> getContactsByQuery(String query) throws APIAuthException;

    List<AmoCRMLead> getLeadsByQuery(String query) throws APIAuthException;

    List<AmoCRMLead> getLeadsByQuery(String query, Long limit, Long offset) throws APIAuthException;

    List<AmoCRMNote> getNotesByTypeAndElementId(String type, Long elementId) throws APIAuthException;

    AmoCRMLead getLeadById(Long leadId) throws APIAuthException;

    AmoCRMCreatedEntityResponse addLead(AmoCRMLead amoCRMLead) throws APIAuthException;

    AmoCRMCreatedLeadsResponse editLeads(AmoCRMEntities amoCRMEntities) throws APIAuthException;

    String getUserLogin();

    void setUserLogin(String userLogin);

    String getUserHash();

    void setUserHash(String userHash);

    String getLoginUrl();

    void setLoginUrl(String loginUrl);

    String getApiBaseUrl();

    void setApiBaseUrl(String apiBaseUrl);

    ArrayList<Long> getLeadClosedStatusesIDs();

    void setLeadClosedStatusesIDs(ArrayList<Long> leadClosedStatusesIDs);

    void setErrorHandler(ResponseErrorHandler errorHandler);

    void setMaxRelogins(Integer maxRelogins);
}
