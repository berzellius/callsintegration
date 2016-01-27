package com.callsintegration.service;

import org.springframework.stereotype.Service;

/**
 * Created by berz on 06.11.2015.
 */
@Service
public interface AddingCallNotesToEmptyLead {
    void start();

    void setPhoneNumberCustomFieldLeads(Long phoneNumberCustomFieldLeads);
}
