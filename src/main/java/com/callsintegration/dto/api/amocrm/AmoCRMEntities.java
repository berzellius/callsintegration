package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMEntities {

    private ArrayList<? extends AmoCRMEntity> add;
    private ArrayList<? extends AmoCRMEntity> update;

    public ArrayList<? extends AmoCRMEntity> getAdd() {
        return add;
    }

    public void setAdd(ArrayList<? extends AmoCRMEntity> add) {
        this.add = add;
    }

    public ArrayList<? extends AmoCRMEntity> getUpdate() {
        return update;
    }

    public void setUpdate(ArrayList<? extends AmoCRMEntity> update) {
        this.update = update;
    }
}
