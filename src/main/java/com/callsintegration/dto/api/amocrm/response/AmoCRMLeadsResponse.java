package com.callsintegration.dto.api.amocrm.response;

import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 07.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMLeadsResponse {
    public AmoCRMLeadsResponse(){}

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response extends AmoCRMEntityResponse{
        private ArrayList<AmoCRMLead> leads;

        public ArrayList<AmoCRMLead> getLeads() {
            return leads;
        }

        public void setLeads(ArrayList<AmoCRMLead> leads) {
            this.leads = leads;
        }
    }
}
