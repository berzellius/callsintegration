package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * Created by berz on 11.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/*
 *  Здесь нам нужен кастомный сериализатор, т.к. значение поля "text" API принимает в старом формате
 *  (экранированные кавычки в json-строке)
 *  Подробнее - https://developers.amocrm.ru/rest_api/notes_type.php#notecall
 */
@JsonSerialize(using = AmoCRMNoteTextSerializer.class)
@JsonDeserialize(using = AmoCRMNoteTextDeserializer.class)
public class AmoCRMNoteText {
    public AmoCRMNoteText(){}

    private String uniq;
    private String phone;
    private String duration;
    private String src;
    private String call_status;
    private String call_result;

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCall_status() {
        return call_status;
    }

    public void setCall_status(String call_status) {
        this.call_status = call_status;
    }

    public String getCall_result() {
        return call_result;
    }

    public void setCall_result(String call_result) {
        this.call_result = call_result;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
