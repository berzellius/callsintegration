package com.callsintegration.dto.api.amocrm.response;

import com.callsintegration.dto.api.amocrm.AmoCRMNote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 06.11.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMNotesResponse {
    public AmoCRMNotesResponse(){}

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response extends AmoCRMEntityResponse{
        private ArrayList<AmoCRMNote> notes;

        public ArrayList<AmoCRMNote> getNotes() {
            return notes;
        }

        public void setNotes(ArrayList<AmoCRMNote> notes) {
            this.notes = notes;
        }
    }
}
