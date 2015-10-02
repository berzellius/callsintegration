package com.callsintegration.dmodel;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by berz on 06.11.14.
 */
@MappedSuperclass
public abstract class DModelEntityFiscalable extends DModelEntity {



    public Date getDtmUpdate() {
        return this.dtmUpdate;
    }

    public void setDtmUpdate(Date dtmUpdate) {
        this.dtmUpdate = dtmUpdate;
    }

    public Date getDtmCreate() {
        return this.dtmCreate;
    }

    public void setDtmCreate(Date dtmCreate) {
        this.dtmCreate = dtmCreate;
    }



    @Column(name = "dtm_create")
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    protected Date dtmCreate;

    @Column(name = "dtm_update")
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    protected Date dtmUpdate;


}
