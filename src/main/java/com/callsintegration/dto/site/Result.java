package com.callsintegration.dto.site;

/**
 * Created by berz on 15.06.2016.
 */
public class Result {
    public Result(String status) {
        this.setStatus(status);
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
