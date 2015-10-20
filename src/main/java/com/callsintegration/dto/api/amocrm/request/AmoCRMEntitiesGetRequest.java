package com.callsintegration.dto.api.amocrm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 04.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMEntitiesGetRequest extends AmoCRMGetRequest {

    private Long limit_rows;
    private Long limit_offset;

    public Long getLimit_rows() {
        return limit_rows;
    }

    public void setLimit_rows(Long limit_rows) {
        this.limit_rows = limit_rows;
    }

    public Long getLimit_offset() {
        return limit_offset;
    }

    public void setLimit_offset(Long limit_offset) {
        this.limit_offset = limit_offset;
    }
}
