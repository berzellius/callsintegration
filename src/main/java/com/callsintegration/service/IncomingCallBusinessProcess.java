package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 10.10.2015.
 */
@Service
public interface IncomingCallBusinessProcess {
    void newIncomingCall(Call call);

    Long getPhoneNumberCustomField();

    Long getDefaultUserId();

    Long getPhoneNumberCustomFieldLeads();

    Long getMarketingChannelContactsCustomField();

    Long getMarketingChannelLeadsCustomField();

    Long getSourceContactsCustomField();

    Long getSourceLeadsCustomField();

    Long getEmailContactCustomField();

    String getEmailContactEnum();

    void setEmailContactEnum(String emailContactEnum);

    void setPhoneNumberContactStockField(Long phoneNumberContactStockField);

    void setPhoneNumberStockFieldContactEnumWork(String phoneNumberStockFieldContactEnumWork);
}
