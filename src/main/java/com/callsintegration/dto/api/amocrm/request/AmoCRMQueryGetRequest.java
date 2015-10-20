package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 07.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AmoCRMQueryGetRequest extends AmoCRMEntitiesGetRequest {
    public AmoCRMQueryGetRequest(){}

    private Long id;
    private String query;
    private Long responsible_user_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getResponsible_user_id() {
        return responsible_user_id;
    }

    public void setResponsible_user_id(Long responsible_user_id) {
        this.responsible_user_id = responsible_user_id;
    }
}
