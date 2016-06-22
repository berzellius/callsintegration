package com.callsintegration.service;

import com.callsintegration.dmodel.LeadFromSite;
import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.callsintegration.exception.APIAuthException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by berz on 06.03.2016.
 */
@Service
public interface AmoCRMLeadsFromSiteService {
    public void processLead(AmoCRMLead amoCRMLead);

    void setUtmSourceCustomFieldId(Long utmSourceCustomFieldId);

    void setUtmMediumCustomFieldId(Long utmMediumCustomFieldId);

    void setUtmCampaignCustomFieldId(Long utmCampaignCustomFieldId);

    void setMarketingChannelCustomFieldId(Long marketingChannelCustomFieldId);

    void setProjectIdToLeadsSource(HashMap<Integer, Long> projectIdToLeadsSource);

    void setSourceLeadsCustomField(Long sourceLeadsCustomField);

    void setNewLeadFromSiteStatusCustomFieldId(Long newLeadFromSiteStatusCustomFieldId);

    void setNewLeadFromSiteStatusCustomFieldEnumNotProcessed(Long newLeadFromSiteStatusCustomFieldEnumNotProcessed);

    LeadFromSite processLeadFromSite(LeadFromSite leadFromSite) throws APIAuthException;
}
