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
    @Autowired
    CallTrackingAPIService callTrackingAPIService;

    @Autowired
    CallsService callsService;

    private Long startIndex;

    private Integer maxResults;

    private Date from;

    private Date to;

    public CallTrackingCallsReader(){}

    public CallTrackingCallsReader(Date from, Date to, Integer maxResults){
        this.setFrom(from);
        this.setTo(to);
        this.setMaxResults(maxResults);
    }

    @Override
    public List<Call> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        //this.setStartIndex(callsService.callsAlreadyLoaded());

        List<Call> calls = callTrackingAPIService.getCalls(this.getFrom(), this.getTo(), this.getMaxResults() );

        System.out.println("read calls: " + calls);

        if(calls.size() > 0) {
            return calls;
        }
        else {
            return null;
        }
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
}
