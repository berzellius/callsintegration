package com.callsintegration.dto.site;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by berz on 15.06.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Call {

    public Call() {
    }

    /*
    * Описание звонка
    */
    private String site;
    private String virtual_number; // Номер, на который звонили
    private String caller; // Номер, с которого звонили
    private String uniqueid; // Уникальный ID
    private Date datetime; // Время
    private Integer project_id; // ID проекта calltracking
    /*
    *
    * Технические поля
     */
    private String result;
    private String processed;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVirtual_number() {
        return virtual_number;
    }

    public void setVirtual_number(String virtual_number) {
        this.virtual_number = virtual_number;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }
}
