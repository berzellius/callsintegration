package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AmoCRMEntity {

    private Long last_modified;

    public Long getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(Long last_modified) {
        this.last_modified = last_modified;
    }
}
