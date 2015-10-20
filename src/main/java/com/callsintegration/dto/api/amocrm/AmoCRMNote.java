package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by berz on 11.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMNote extends AmoCRMEntity{
    public AmoCRMNote(){};

    // ID контакта или сделки
    private Long element_id;
    // 1 - контакт, 2 - сделка
    private Integer element_type;
    // 10 - входящий звонок
    private Integer note_type;

    private AmoCRMNoteText text;

    public Long getElement_id() {
        return element_id;
    }

    public void setElement_id(Long element_id) {
        this.element_id = element_id;
    }

    public Integer getElement_type() {
        return element_type;
    }

    public void setElement_type(Integer element_type) {
        this.element_type = element_type;
    }

    public Integer getNote_type() {
        return note_type;
    }

    public void setNote_type(Integer note_type) {
        this.note_type = note_type;
    }

    public AmoCRMNoteText getText() {
        return text;
    }

    public void setText(AmoCRMNoteText text) {
        this.text = text;
    }
}
