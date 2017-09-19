package com.callsintegration.service;

import com.callsintegration.dto.site.Call;
import com.callsintegration.dto.site.CallRequest;
import com.callsintegration.dto.site.Result;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.scheduling.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by berz on 28.09.2016.
 */
@Service
public class WebhookServiceImpl implements WebhookService {

    @Autowired
    CallRepository callRepository;

    @Autowired
    ScheduledTasks scheduledTasks;

    @Autowired
    CallTrackingAPIService callTrackingAPIService;

    private static final Logger log = LoggerFactory.getLogger(WebhookServiceImpl.class);


    /*
    *
    * Не является transactional, так как из transactional метода не запустится scheduledTask
     */
    @Override
    public Result newCallFromWebhook(CallRequest callRequest) {
        log.info("we dont need to do whatever, just endup processing");
        return new Result("success");
        /*
        List<Call> calls = callRequest.getCalls();

        if(calls != null && calls.size() > 0){
            System.out.println("webhook! " + callRequest.toString());
            processCalls(calls);
            scheduledTasks.runImportCallsToCRM();
        }

        return new Result("success");*/
    }


    @Transactional
    private void processCalls(List<Call> calls){

        for(Call call : calls){
            if(call.getCaller() == null || call.getCaller().equals("")){
                call.setCaller("unknown");
            }
            com.callsintegration.dmodel.Call c = new com.callsintegration.dmodel.Call();
            c.setNumber(call.getCaller());
            c.setDt(call.getDatetime());
            c.setProjectId(call.getProject_id());
            c.setState(com.callsintegration.dmodel.Call.State.NEW);

            callTrackingAPIService.processCallOnImport(c);
            callRepository.save(c);
        }
    }

}
