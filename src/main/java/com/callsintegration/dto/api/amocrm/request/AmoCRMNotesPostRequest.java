package com.callsintegration.dto.api.amocrm.request;

import com.callsintegration.dto.api.amocrm.AmoCRMEntities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMNotesPostRequest extends AmoCRMEntityPostRequest {
    public AmoCRMNotesPostRequest(){}

    public AmoCRMNotesPostRequest(AmoCRMEntities amoCRMEntities){
        this.setNotes(amoCRMEntities);
    }

    private AmoCRMEntities notes;

    public AmoCRMEntities getNotes() {
        return notes;
    }

    public void setNotes(AmoCRMEntities notes) {
        this.notes = notes;
    }
}
