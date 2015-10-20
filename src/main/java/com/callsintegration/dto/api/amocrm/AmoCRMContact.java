package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by berz on 29.09.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMContact extends AmoCRMEntityWCustomFields {

    public AmoCRMContact(){}

    private Long id;

    private String name;
    private String company_name;
    private String type;
    private Long created_user_id;
    private ArrayList<Long> linked_leads_id;
    private Long responsible_user_id;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreated_user_id() {
        return created_user_id;
    }

    public void setCreated_user_id(Long created_user_id) {
        this.created_user_id = created_user_id;
    }

    public ArrayList<Long> getLinked_leads_id() {
        return linked_leads_id;
    }

    public void setLinked_leads_id(ArrayList<Long> linked_leads_id) {
        this.linked_leads_id = linked_leads_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResponsible_user_id() {
        return responsible_user_id;
    }

    public void setResponsible_user_id(Long responsible_user_id) {
        this.responsible_user_id = responsible_user_id;
    }

    public void addLinkedLeadById(Long id) {
        ArrayList<Long> linkedLeads = this.getLinked_leads_id();

        if(linkedLeads == null){
            linkedLeads = new ArrayList<>();
        }

        linkedLeads.add(id);
        this.setLinked_leads_id(linkedLeads);
    }
}
