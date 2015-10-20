package com.callsintegration.dto.api.amocrm.response;

import com.callsintegration.dto.api.amocrm.AmoCRMContactsLeadsLink;

import java.util.ArrayList;

/**
 * Created by berz on 06.10.2015.
 */
public class AmoCRMContactsLeadsLinksResponse extends  AmoCRMEntityResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response extends AmoCRMEntityResponse{
        private ArrayList<AmoCRMContactsLeadsLink> links;

        public ArrayList<AmoCRMContactsLeadsLink> getLinks() {
            return links;
        }

        public void setLinks(ArrayList<AmoCRMContactsLeadsLink> links) {
            this.links = links;
        }
    }

}
