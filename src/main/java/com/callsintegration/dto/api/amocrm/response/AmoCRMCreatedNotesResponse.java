package com.callsintegration.dto.api.amocrm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 10.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMCreatedNotesResponse {
    public AmoCRMCreatedNotesResponse(){}

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response extends AmoCRMResponse{
        private Notes notes;

        public Notes getNotes() {
            return notes;
        }

        public void setNotes(Notes notes) {
            this.notes = notes;
        }

        public class Notes {

            private ArrayList<AmoCRMCreatedEntityResponse> add;
            private ArrayList<AmoCRMCreatedEntityResponse> update;

            public ArrayList<AmoCRMCreatedEntityResponse> getAdd() {
                return add;
            }

            public void setAdd(ArrayList<AmoCRMCreatedEntityResponse> add) {
                this.add = add;
            }

            public ArrayList<AmoCRMCreatedEntityResponse> getUpdate() {
                return update;
            }

            public void setUpdate(ArrayList<AmoCRMCreatedEntityResponse> update) {
                this.update = update;
            }
        }
    }
}
