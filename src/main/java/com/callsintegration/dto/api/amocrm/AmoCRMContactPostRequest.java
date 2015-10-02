package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContactPostRequest extends AmoCRMEntityPostRequest {

    private AmoCRMEntities contacts;

    public AmoCRMEntities getContacts() {
        return contacts;
    }

    public void setContacts(AmoCRMEntities contacts) {
        this.contacts = contacts;
    }

}
