package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 06.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContactsLeadsLink extends AmoCRMEntity {
    public AmoCRMContactsLeadsLink(){}

    private Long contact_id;
    private Long lead_id;

    public Long getContact_id() {
        return contact_id;
    }

    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
    }

    public Long getLead_id() {
        return lead_id;
    }

    public void setLead_id(Long lead_id) {
        this.lead_id = lead_id;
    }
}
