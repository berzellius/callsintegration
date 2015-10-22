package com.callsintegration.dto.api.amocrm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * Created by berz on 04.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMResponse {


    private Integer server_time;

    private String error;

    public Integer getServer_time() {
        return server_time;
    }

    public void setServer_time(Integer server_time) {
        this.server_time = server_time;
    }

    @Override
    public String toString(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return  objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{corrupted_data}";
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
