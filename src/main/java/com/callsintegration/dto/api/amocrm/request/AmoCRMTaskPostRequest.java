package com.callsintegration.dto.api.amocrm.request;

import com.callsintegration.dto.api.amocrm.AmoCRMEntities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 11.09.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMTaskPostRequest extends AmoCRMEntityPostRequest {

    private AmoCRMEntities tasks;

    public AmoCRMEntities getTasks() {
        return tasks;
    }

    public void setTasks(AmoCRMEntities tasks) {
        this.tasks = tasks;
    }
}
