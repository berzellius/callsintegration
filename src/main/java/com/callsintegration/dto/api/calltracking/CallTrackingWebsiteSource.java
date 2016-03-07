package com.callsintegration.dto.api.calltracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 23.02.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallTrackingWebsiteSource {

    private Integer clientId;
    private Integer projectId;
    private Integer sourceId;
    private String sourceName;
    private String utm_term;
    private Integer utm_term_type;
    private Integer sourceSys;
    private String isOnlineType;
    private String isDynamicType;
    private String allowMultiUse;
    private Integer sourceMsgId;
    private Integer addPriority;
    private String geoCityID;
    private String deleted_at;
    private Integer phones_count;
    private Integer numerator;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUtm_term() {
        return utm_term;
    }

    public void setUtm_term(String utm_term) {
        this.utm_term = utm_term;
    }

    public Integer getUtm_term_type() {
        return utm_term_type;
    }

    public void setUtm_term_type(Integer utm_term_type) {
        this.utm_term_type = utm_term_type;
    }

    public Integer getSourceSys() {
        return sourceSys;
    }

    public void setSourceSys(Integer sourceSys) {
        this.sourceSys = sourceSys;
    }

    public String getIsOnlineType() {
        return isOnlineType;
    }

    public void setIsOnlineType(String isOnlineType) {
        this.isOnlineType = isOnlineType;
    }

    public String getIsDynamicType() {
        return isDynamicType;
    }

    public void setIsDynamicType(String isDynamicType) {
        this.isDynamicType = isDynamicType;
    }

    public String getAllowMultiUse() {
        return allowMultiUse;
    }

    public void setAllowMultiUse(String allowMultiUse) {
        this.allowMultiUse = allowMultiUse;
    }

    public Integer getSourceMsgId() {
        return sourceMsgId;
    }

    public void setSourceMsgId(Integer sourceMsgId) {
        this.sourceMsgId = sourceMsgId;
    }

    public Integer getAddPriority() {
        return addPriority;
    }

    public void setAddPriority(Integer addPriority) {
        this.addPriority = addPriority;
    }

    public String getGeoCityID() {
        return geoCityID;
    }

    public void setGeoCityID(String geoCityID) {
        this.geoCityID = geoCityID;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Integer getPhones_count() {
        return phones_count;
    }

    public void setPhones_count(Integer phones_count) {
        this.phones_count = phones_count;
    }

    public Integer getNumerator() {
        return numerator;
    }

    public void setNumerator(Integer numerator) {
        this.numerator = numerator;
    }
}
