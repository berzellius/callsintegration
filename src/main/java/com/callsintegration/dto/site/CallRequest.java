package com.callsintegration.dto.site;

import java.util.List;

/**
 * Created by berz on 15.06.2016.
 */
public class CallRequest {
    public CallRequest() {
    }

    private List<Call> calls;

    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }
}
