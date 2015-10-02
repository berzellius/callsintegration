package com.callsintegration.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by berz on 20.10.14.
 */
@Component
public class AddTemplatesDataInterceptor extends HandlerInterceptorAdapter {


    @Override
    public void postHandle(HttpServletRequest request,
                    HttpServletResponse response,
                    Object handler,
                    ModelAndView modelAndView)
            throws Exception{


    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                          HttpServletResponse response, Object handler){
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");
        return true;
    }
}
