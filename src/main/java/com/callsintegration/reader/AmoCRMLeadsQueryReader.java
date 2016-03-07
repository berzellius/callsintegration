package com.callsintegration.reader;

import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.callsintegration.service.AmoCRMService;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by berz on 06.03.2016.
 */
@Component
public class AmoCRMLeadsQueryReader implements ResetableItemReader<List<AmoCRMLead>> {

    private String query;
    private Long leadsByRequest = 1l;

    /*
    * Current offset
     */
    private Long offset = 0l;

    @Autowired
    AmoCRMService amoCRMService;

    public AmoCRMLeadsQueryReader(){}

    public AmoCRMLeadsQueryReader(String query){
        this.setQuery(query);
    }

    @Override
    public List<AmoCRMLead> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(this.offset == null || this.getLeadsByRequest() == null || this.getQuery() == null){
            return null;
        }

        Long offset = this.offset;
        this.offset += this.getLeadsByRequest();
        List<AmoCRMLead> leads = amoCRMService.getLeadsByQuery(this.getQuery(), this.getLeadsByRequest(), offset);

        if(leads.size() == 0){
            this.reset();
            return null;
        }

        return leads;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getLeadsByRequest() {
        return leadsByRequest;
    }

    public void setLeadsByRequest(Long leadsByRequest) {
        this.leadsByRequest = leadsByRequest;
    }

    @Override
    public void reset() {
        System.out.println("reset reader...");
        this.offset = 0l;
    }
}
