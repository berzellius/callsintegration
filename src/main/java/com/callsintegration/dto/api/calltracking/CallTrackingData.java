package com.callsintegration.dto.api.calltracking;

import com.callsintegration.dmodel.Call;
import com.callsintegration.jackson.deserializer.CallDataDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * Created by berz on 20.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallTrackingData extends CallTrackingResponse {

    @JsonDeserialize(using = CallDataDeserializer.class)
    private List<Call> data;

    public List<Call> getData() {
        return data;
    }

    public void setData(List<Call> data) {
        this.data = data;
    }

}
