package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContact extends AmoCRMEntity {

    public AmoCRMContact(){}

    private String name;
    private String company_name;
    private ArrayList<AmoCRMCustomField> custom_fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public ArrayList<AmoCRMCustomField> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<AmoCRMCustomField> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
