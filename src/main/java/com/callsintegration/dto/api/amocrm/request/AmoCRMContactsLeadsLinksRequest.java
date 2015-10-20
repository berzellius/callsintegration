package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 06.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContactsLeadsLinksRequest extends AmoCRMEntitiesGetRequest {

    private Long contacts_link;
    private Long deals_link;

    public Long getContacts_link() {
        return contacts_link;
    }

    public void setContacts_link(Long contacts_link) {
        this.contacts_link = contacts_link;
    }

    public Long getDeals_link() {
        return deals_link;
    }

    public void setDeals_link(Long deals_link) {
        this.deals_link = deals_link;
    }
}
