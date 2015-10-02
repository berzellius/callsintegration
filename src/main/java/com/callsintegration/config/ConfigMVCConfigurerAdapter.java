package com.callsintegration.config;


import com.callsintegration.interceptors.AddTemplatesDataInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
public class ConfigMVCConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Autowired
    AddTemplatesDataInterceptor addTemplatesDataInterceptor;

    @Autowired
    LocaleChangeInterceptor localeChangeInterceptor;


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {

        registry.addInterceptor(addTemplatesDataInterceptor);

        registry.addInterceptor(localeChangeInterceptor);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/WEB-INF/static/");
    }

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry){

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new PageRequest(0, 5));
        argumentResolvers.add(resolver);
    }


}
