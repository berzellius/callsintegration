package com.callsintegration.reader;

import com.callsintegration.dmodel.Call;
import com.callsintegration.service.CallTrackingAPIService;
import com.callsintegration.service.CallsService;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by berz on 27.09.2015.
 */
@Component
public class CallTrackingCallsReader implements ItemReader<List<Call>> {

    public static enum DateMode{
        UPDATE_EACH_READ,
        NO_UPDATE
    }

    private DateMode dateMode;

    @Autowired
    CallTrackingAPIService callTrackingAPIService;

    @Autowired
    CallsService callsService;

    private Long startIndex;

    private Integer maxResults;

    private Date from;

    private Date to;

    public CallTrackingCallsReader(){}

    public CallTrackingCallsReader(Date from, Date to, Integer maxResults, DateMode dm){
        this.setFrom(from);
        this.setTo(to);
        this.setMaxResults(maxResults);

        if(dm == null){
            dm = DateMode.UPDATE_EACH_READ;
        }

        this.setDateMode(dm);
    }

    @Override
    public List<Call> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(this.getDateMode() != null && this.getDateMode().equals(DateMode.UPDATE_EACH_READ)){
            this.setDatesNow();
        }

        /*
        * getCalls загружает звонки, которые еще не сохранены в БД
        * повторный вызов без сохранения звонков в базе возвратит повторяющийся блок данных.
         */

        List<Call> calls = callTrackingAPIService.getCalls(this.getFrom(), this.getTo(), this.getMaxResults() );

        System.out.println("read calls: " + calls);

        if(calls.size() > 0) {
            return calls;
        }
        else {
            return null;
        }
    }

    private void setDatesNow() {
        Date dt = new Date();
        System.out.println("updated dates: " + dt);
        this.setFrom(dt);
        this.setTo(dt);
    }

    public Long getStartIndex() {
        return this.startIndex;
    }

    public void setStartIndex(Long startIndex) {
        System.out.println("setting start index :".concat(startIndex.toString()));
        this.startIndex = startIndex;
    }

    public Integer getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Date getFrom() {
        return this.from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return this.to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public DateMode getDateMode() {
        return dateMode;
    }

    public void setDateMode(DateMode dateMode) {
        this.dateMode = dateMode;
    }
}
