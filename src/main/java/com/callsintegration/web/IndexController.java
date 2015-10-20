package com.callsintegration.web;

import com.callsintegration.dmodel.Call;
import com.callsintegration.dto.api.amocrm.AmoCRMContact;
import com.callsintegration.dto.api.amocrm.AmoCRMContactsLeadsLink;
import com.callsintegration.dto.api.amocrm.response.AmoCRMContactsResponse;
import com.callsintegration.exception.APIAuthException;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.service.AmoCRMService;
import com.callsintegration.service.CallTrackingAPIService;
import com.callsintegration.service.IncomingCallBusinessProcess;
import com.callsintegration.specifications.CallSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by berz on 20.09.2015.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {

    @Autowired
    CallTrackingAPIService callTrackingAPIService;

    @Autowired
    CallRepository callRepository;

    @Autowired
    AmoCRMService amoCRMService;

    @Autowired
    IncomingCallBusinessProcess incomingCallBusinessProcess;



    @RequestMapping
    public String indexPage(
            Model model,
            @RequestParam(value = "from", required = false)
            Long from,
            @RequestParam(value = "max", required = false)
            Integer max,
            @RequestParam(value = "testcall", required = false)
            Long callToTestID
    ) throws ParseException {


        model.addAttribute("calls", callRepository.findAll(
                CallSpecifications.byDate(new Date())
        ));

        /*try {
            AmoCRMContact amoCRMContact = new AmoCRMContact();
            amoCRMContact.setName("Sharashkin & Co");
            amoCRMService.addContact(amoCRMContact);
        } catch (APIAuthException e) {
            e.printStackTrace();
        }*/


            /*List<AmoCRMContact> contacts = amoCRMService.getContactsByQuery("1234567890");
            System.out.println(contacts.size() + " found");
            for(AmoCRMContact contact :  contacts){
                System.out.println("contact[" + contact.getId().toString() + "]");
                List<AmoCRMContactsLeadsLink> links = amoCRMService.getContactsLeadsLinksByContact(contact);
                for(AmoCRMContactsLeadsLink link : links){
                    System.out.println("link: contact[" + link.getContact_id().toString() + "]<->lead[" + link.getLead_id().toString() + "]");
                }
            }*/
            if(callToTestID != null){
                Call call = callRepository.findOne(callToTestID);
                if(call != null){
                    incomingCallBusinessProcess.newIncomingCall(call);
                }
            }


        return "index";
    }

}
