package com.callsintegration.dto.api.amocrm.request;

import com.callsintegration.dto.api.amocrm.AmoCRMEntities;
import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 07.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMLeadPostRequest extends AmoCRMEntityPostRequest {
    public AmoCRMLeadPostRequest(){}

    public AmoCRMLeadPostRequest(AmoCRMEntities leads){
        this.leads = leads;
    }

    private AmoCRMEntities leads;

    public AmoCRMEntities getLeads() {
        return leads;
    }

    public void setLeads(AmoCRMEntities leads) {
        this.leads = leads;
    }
}
