package com.callsintegration.dto.site;

import java.util.List;

/**
 * Created by berz on 15.06.2016.
 */
public class LeadRequest {
    public LeadRequest() {
    }

    private List<Lead> leads;
    private String origin;
    private String password;

    public List<Lead> getLeads() {
        return leads;
    }

    public void setLeads(List<Lead> leads) {
        this.leads = leads;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
