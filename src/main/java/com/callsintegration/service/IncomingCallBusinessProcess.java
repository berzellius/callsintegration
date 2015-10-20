package com.callsintegration.service;

import com.callsintegration.dmodel.Call;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 10.10.2015.
 */
@Service
public interface IncomingCallBusinessProcess {
    void newIncomingCall(Call call);
}
