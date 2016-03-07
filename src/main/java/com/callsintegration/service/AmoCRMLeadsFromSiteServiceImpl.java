package com.callsintegration.service;

import com.callsintegration.dmodel.CallTrackingSourceCondition;
import com.callsintegration.dto.api.amocrm.AmoCRMCustomField;
import com.callsintegration.dto.api.amocrm.AmoCRMCustomFieldValue;
import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.callsintegration.dto.api.amocrm.AmoCRMTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by berz on 06.03.2016.
 */
@Service
public class AmoCRMLeadsFromSiteServiceImpl implements AmoCRMLeadsFromSiteService {

    private Long utmSourceCustomFieldId;
    private Long utmMediumCustomFieldId;
    private Long utmCampaignCustomFieldId;
    private Long marketingChannelCustomFieldId;
    private Long newLeadFromSiteStatusCustomFieldId;
    private Long newLeadFromSiteStatusCustomFieldEnumNotProcessed;

    private Long sourceLeadsCustomField;

    private HashMap<Integer, Long> projectIdToLeadsSource;

    @Autowired
    CallTrackingSourceConditionService callTrackingSourceConditionService;


    @Override
    public void processLead(AmoCRMLead amoCRMLead) {
        System.out.println("lead #" + amoCRMLead.getId());

        String utmSource = "";
        String utmMedium = "";
        String utmCampaign = "";

        Integer projectId = null;

        for(AmoCRMCustomField amoCRMCustomField : amoCRMLead.getCustom_fields()){
            if(amoCRMCustomField.getId().equals(this.getUtmSourceCustomFieldId()) && amoCRMCustomField.getValues().size() > 0){
                utmSource = amoCRMCustomField.getValues().get(0).getValue();
            }

            if(amoCRMCustomField.getId().equals(this.getUtmMediumCustomFieldId()) && amoCRMCustomField.getValues().size() > 0){
                utmMedium = amoCRMCustomField.getValues().get(0).getValue();
            }

            if(amoCRMCustomField.getId().equals(this.getUtmCampaignCustomFieldId()) && amoCRMCustomField.getValues().size() > 0){
                utmCampaign = amoCRMCustomField.getValues().get(0).getValue();
            }

            if(amoCRMCustomField.getId().equals(this.getSourceLeadsCustomField()) && amoCRMCustomField.getValues().size() > 0){
                Long projectIdFromLead = Long.decode(amoCRMCustomField.getValues().get(0).getEnumerated());

                for(Integer project : this.getProjectIdToLeadsSource().keySet()){
                    if(this.getProjectIdToLeadsSource().get(project).equals(projectIdFromLead)){
                        projectId = project;
                    }
                }
            }
        }

        if(projectId == null){
            System.out.println("Project custom field is empty or has wrong value!");
            return;
        }

        if(!amoCRMLead.checkCustomFieldEnumValue(this.getNewLeadFromSiteStatusCustomFieldId(), this.getNewLeadFromSiteStatusCustomFieldEnumNotProcessed().toString())){
            System.out.println("'new lead from site status' field has wrong value");
            return;
        }


        System.out.println("utm = {" + utmSource + ", " + utmMedium + ", " + utmCampaign + "}, project = " + projectId);

        CallTrackingSourceCondition callTrackingSourceCondition = callTrackingSourceConditionService.getCallTrackingSourceConditionByUtmAndProjectId(utmSource, utmMedium, utmCampaign, projectId);

        if(callTrackingSourceCondition != null){
            String[] values = {callTrackingSourceCondition.getSourceName()};
            amoCRMLead.addStringValuesToCustomField(this.getMarketingChannelCustomFieldId(), values);

            amoCRMLead.setEmptyValueToField(this.getNewLeadFromSiteStatusCustomFieldId());
        }
    }

    public Long getUtmSourceCustomFieldId() {
        return utmSourceCustomFieldId;
    }

    @Override
    public void setUtmSourceCustomFieldId(Long utmSourceCustomFieldId) {
        this.utmSourceCustomFieldId = utmSourceCustomFieldId;
    }

    public Long getUtmMediumCustomFieldId() {
        return utmMediumCustomFieldId;
    }

    @Override
    public void setUtmMediumCustomFieldId(Long utmMediumCustomFieldId) {
        this.utmMediumCustomFieldId = utmMediumCustomFieldId;
    }

    public Long getUtmCampaignCustomFieldId() {
        return utmCampaignCustomFieldId;
    }

    @Override
    public void setUtmCampaignCustomFieldId(Long utmCampaignCustomFieldId) {
        this.utmCampaignCustomFieldId = utmCampaignCustomFieldId;
    }

    public Long getMarketingChannelCustomFieldId() {
        return marketingChannelCustomFieldId;
    }

    @Override
    public void setMarketingChannelCustomFieldId(Long marketingChannelCustomFieldId) {
        this.marketingChannelCustomFieldId = marketingChannelCustomFieldId;
    }

    public HashMap<Integer, Long> getProjectIdToLeadsSource() {
        return projectIdToLeadsSource;
    }

    @Override
    public void setProjectIdToLeadsSource(HashMap<Integer, Long> projectIdToLeadsSource) {
        this.projectIdToLeadsSource = projectIdToLeadsSource;
    }

    public Long getSourceLeadsCustomField() {
        return sourceLeadsCustomField;
    }

    @Override
    public void setSourceLeadsCustomField(Long sourceLeadsCustomField) {
        this.sourceLeadsCustomField = sourceLeadsCustomField;
    }

    public Long getNewLeadFromSiteStatusCustomFieldId() {
        return newLeadFromSiteStatusCustomFieldId;
    }

    @Override
    public void setNewLeadFromSiteStatusCustomFieldId(Long newLeadFromSiteStatusCustomFieldId) {
        this.newLeadFromSiteStatusCustomFieldId = newLeadFromSiteStatusCustomFieldId;
    }

    public Long getNewLeadFromSiteStatusCustomFieldEnumNotProcessed() {
        return newLeadFromSiteStatusCustomFieldEnumNotProcessed;
    }

    @Override
    public void setNewLeadFromSiteStatusCustomFieldEnumNotProcessed(Long newLeadFromSiteStatusCustomFieldEnumNotProcessed) {
        this.newLeadFromSiteStatusCustomFieldEnumNotProcessed = newLeadFromSiteStatusCustomFieldEnumNotProcessed;
    }
}
