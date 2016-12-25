package com.callsintegration.web;

import com.callsintegration.dto.site.CallRequest;
import com.callsintegration.dto.site.LeadRequest;
import com.callsintegration.dto.site.Result;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.service.CallsService;
import com.callsintegration.service.WebhookService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by berz on 15.06.2016.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/")
public class RestController extends BaseController {

    @Autowired
    CallsService callsService;

    @Autowired
    WebhookService webhookService;

    @RequestMapping(
            value = "lead_from_site",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newLeadFromSite(
            @RequestBody
            LeadRequest leadRequest
    ){
        return callsService.newLeadFromSite(leadRequest.getLeads(), leadRequest.getOrigin(), leadRequest.getPassword());
    }

    @RequestMapping(
            value = "call_webhook",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newCallWebhook(
            @RequestBody
            CallRequest callRequest
    ) throws NotFoundException {
        System.out.println("webhook! " + callRequest.toString());
        throw new NotFoundException("out of service!");
        //return webhookService.newCallFromWebhook(callRequest);
    }
}
