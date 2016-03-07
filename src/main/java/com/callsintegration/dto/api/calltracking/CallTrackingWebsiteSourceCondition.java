package com.callsintegration.dto.api.calltracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 23.02.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallTrackingWebsiteSourceCondition {
    private Integer conditionId;
    private Integer sourceId;
    private String utm_source;
    private String utm_medium;
    private String utm_campaign;
    private String deleted_at;

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public String getUtm_medium() {
        return utm_medium;
    }

    public void setUtm_medium(String utm_medium) {
        this.utm_medium = utm_medium;
    }

    public String getUtm_campaign() {
        return utm_campaign;
    }

    public void setUtm_campaign(String utm_campaign) {
        this.utm_campaign = utm_campaign;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
