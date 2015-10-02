package com.callsintegration.dmodel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by berz on 07.11.14.
 */
@MappedSuperclass
public abstract class DModelEntityNamed extends DModelEntityFiscalable {

    protected String name;

    @Column(columnDefinition = "character varying(2000)")
    protected String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
