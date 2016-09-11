package com.callsintegration.dto.api.amocrm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 11.09.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMCreatedTasksResponse {
    public AmoCRMCreatedTasksResponse(){}

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response extends AmoCRMResponse{
        private Tasks tasks;

        public Tasks getTasks() {
            return tasks;
        }

        public void setTasks(Tasks tasks) {
            this.tasks = tasks;
        }

        public class Tasks{
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
