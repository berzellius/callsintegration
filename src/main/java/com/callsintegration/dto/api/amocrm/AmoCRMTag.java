package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by berz on 06.03.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMTag {
    public AmoCRMTag() {
    }

    private String name;
    private Long id;

    public AmoCRMTag(Long tagId, String tagName) {
        this.setName(tagName);
        this.setId(tagId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
