package com.callsintegration.web;

import com.callsintegration.dmodel.Call;
import com.callsintegration.exception.APIAuthException;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.service.CallTrackingAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    @RequestMapping
    public String indexPage(
            Model model,
            @RequestParam(value = "from", required = false)
            Long from,
            @RequestParam(value = "max", required = false)
            Integer max
    ) throws ParseException {


        model.addAttribute("calls", callRepository.findAll());

        return "index";
    }

}
