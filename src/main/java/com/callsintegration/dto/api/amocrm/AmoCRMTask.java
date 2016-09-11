package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by berz on 11.09.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMTask extends AmoCRMEntity {
    public AmoCRMTask() {
    }

    public void setLead(AmoCRMLead lead){
        this.setElement_id(lead.getId());
        this.setElement_type(2l);
    }

    public void setContact(AmoCRMContact contact){
        this.setElement_id(contact.getId());
        this.setElement_type(1l);
    }

    private Long id;

    // ID контакта или сделки
    private Long element_id;

    // 1 - контакт, 2 - сделка, 3 - компания
    private Long element_type;

    private Long date_create;

    private Long status;

    // см. AmoCRMAccount
    private Long task_type;

    private String text;

    private Long responsible_user_id;

    private Long complete_till;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getElement_id() {
        return element_id;
    }

    public void setElement_id(Long element_id) {
        this.element_id = element_id;
    }

    public Long getElement_type() {
        return element_type;
    }

    public void setElement_type(Long element_type) {
        this.element_type = element_type;
    }

    public Long getDate_create() {
        return date_create;
    }

    public void setDate_create(Long date_create) {
        this.date_create = date_create;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTask_type() {
        return task_type;
    }

    public void setTask_type(Long task_type) {
        this.task_type = task_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getResponsible_user_id() {
        return responsible_user_id;
    }

    public void setResponsible_user_id(Long responsible_user_id) {
        this.responsible_user_id = responsible_user_id;
    }

    public Long getComplete_till() {
        return complete_till;
    }

    public void setComplete_till(Long complete_till) {
        this.complete_till = complete_till;
    }

    public void setComplete_till(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        this.setComplete_till(calendar.getTime().getTime()/1000);
    }
}
