package com.callsintegration.dto.api.amocrm.response;

import com.callsintegration.dto.api.amocrm.AmoCRMContact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 04.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContactsResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    public class Response extends AmoCRMEntityResponse{
        private ArrayList<AmoCRMContact> contacts;

        public ArrayList<AmoCRMContact> getContacts() {
            return contacts;
        }

        public void setContacts(ArrayList<AmoCRMContact> contacts) {
            this.contacts = contacts;
        }
    }
}
