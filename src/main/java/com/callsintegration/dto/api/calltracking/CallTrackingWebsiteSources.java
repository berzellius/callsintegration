package com.callsintegration.dto.api.calltracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by berz on 23.02.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallTrackingWebsiteSources {

    private List<String> test;
    private Integer page;
    private Integer records;
    private Integer total;

    private List<CallTrackingWebsiteSource> rows;

    public List<String> getTest() {
        return test;
    }

    public void setTest(List<String> test) {
        this.test = test;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<CallTrackingWebsiteSource> getRows() {
        return rows;
    }

    public void setRows(List<CallTrackingWebsiteSource> rows) {
        this.rows = rows;
    }
}
