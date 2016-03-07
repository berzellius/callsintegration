package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by berz on 11.10.2015.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmoCRMEntityWCustomFieldsAndTags extends AmoCRMEntityWCustomFields {
    public AmoCRMEntityWCustomFieldsAndTags(){}

    private ArrayList<AmoCRMTag> tags;

    public ArrayList<AmoCRMTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<AmoCRMTag> tags) {
        this.tags = tags;
    }


    public boolean hasTagById(Long tagId) {
        for(AmoCRMTag amoCRMTag : this.getTags()){
            if(amoCRMTag.getId().equals(tagId)){
                return true;
            }
        }
        return false;
    }

    public void removeTagById(Long tagId) {
        Iterator<AmoCRMTag> iterator = this.getTags().iterator();
        while (iterator.hasNext()){
            AmoCRMTag amoCRMTag = iterator.next();
            if(amoCRMTag.getId().equals(tagId))
                iterator.remove();
        }
    }
}
