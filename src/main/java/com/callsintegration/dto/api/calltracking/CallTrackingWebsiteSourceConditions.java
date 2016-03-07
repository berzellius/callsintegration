package com.callsintegration.dto.api.calltracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by berz on 23.02.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallTrackingWebsiteSourceConditions {
    private List<CallTrackingWebsiteSourceCondition> conditions;

    public List<CallTrackingWebsiteSourceCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<CallTrackingWebsiteSourceCondition> conditions) {
        this.conditions = conditions;
    }
}
