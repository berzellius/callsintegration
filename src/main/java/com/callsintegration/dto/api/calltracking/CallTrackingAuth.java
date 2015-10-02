package com.callsintegration.dto.api.calltracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 20.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallTrackingAuth extends CallTrackingResponse {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
