package com.callsintegration.service;

import com.callsintegration.dto.site.Call;
import com.callsintegration.dto.site.CallRequest;
import com.callsintegration.dto.site.Result;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.scheduling.ScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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


    /*
    *
    * Не является transactional, так как из transactional метода не запустится scheduledTask
     */
    @Override
    public Result newCallFromWebhook(CallRequest callRequest) {
        List<Call> calls = callRequest.getCalls();

        if(calls != null){
            processCalls(calls);
            scheduledTasks.runImportCallsToCRM();
        }

        return new Result("success");
    }


    @Transactional
    private void processCalls(List<Call> calls){
        for(Call call : calls){
            com.callsintegration.dmodel.Call c = new com.callsintegration.dmodel.Call();
            c.setNumber(call.getCaller());
            c.setDt(call.getDatetime());
            c.setProjectId(call.getProject_id());
            c.setState(com.callsintegration.dmodel.Call.State.NEW);

            callRepository.save(c);
        }
    }

}
