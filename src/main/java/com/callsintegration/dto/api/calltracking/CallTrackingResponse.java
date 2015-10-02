package com.callsintegration.dto.api.calltracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 20.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CallTrackingResponse {

    private String status;
    private Integer error_code;
    private String error_text;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_text() {
        return error_text;
    }

    public void setError_text(String error_text) {
        this.error_text = error_text;
    }
}
