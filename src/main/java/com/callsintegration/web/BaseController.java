package com.callsintegration.web;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by berz on 17.05.15.
 */
@Controller
public abstract class BaseController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(
    ) {
        return "notfound";
    }
}
