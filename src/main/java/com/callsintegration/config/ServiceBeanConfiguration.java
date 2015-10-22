package com.callsintegration.config;


import com.callsintegration.dto.api.ErrorHandlers.AmoCRMAPIRequestErrorHandler;
import com.callsintegration.dto.api.ErrorHandlers.CalltrackingAPIRequestErrorHandler;
import com.callsintegration.interceptors.AddTemplatesDataInterceptor;
import com.callsintegration.service.*;
import com.callsintegration.settings.LocalProjectSettings;
import com.callsintegration.settings.ProjectSettings;
import com.callsintegration.settings.RemoteProjectSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by berz on 20.10.14.
 */
@Configuration
public class ServiceBeanConfiguration {

    @Bean
    AddTemplatesDataInterceptor addTemplatesDataInterceptor(){
        return new AddTemplatesDataInterceptor();
    }


    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        String[] resources = {"classpath:/labels","classpath:/message"};
        messageSource.setBasenames(resources);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("locale");
        return localeChangeInterceptor;
    }

    @Bean
    public SessionLocaleResolver sessionLocaleResolver(){
        SessionLocaleResolver localeResolver=new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("ru","RU"));
        return localeResolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSize(10 * 1024 * 1024);

        return commonsMultipartResolver;
    }

    @Bean
    public ProjectSettings projectSettings(){
        // Локальный сервер
        return new LocalProjectSettings();
        // Боевой сервер
        //return new RemoteProjectSettings();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public CallTrackingAPIService callTrackingAPIService(){
        CallTrackingAPIService callTrackingAPIService = new CallTrackingAPIServiceImpl();

        callTrackingAPIService.setApiMethod(HttpMethod.POST);
        callTrackingAPIService.setApiURL("https://calltracking.ru/api/get_data.php");

        callTrackingAPIService.setLoginURL("https://calltracking.ru/api/login.php");
        callTrackingAPIService.setLoginMethod(HttpMethod.POST);

        callTrackingAPIService.setLogin("info@home-motion.ru");
        callTrackingAPIService.setPassword("SsX0d1XE75");
        Integer[] projects = {3901, 3400};
        callTrackingAPIService.setProjects(projects);

        CalltrackingAPIRequestErrorHandler errorHandler = new CalltrackingAPIRequestErrorHandler();
        callTrackingAPIService.setErrorHandler(errorHandler);

        return callTrackingAPIService;
    }

    @Bean
    public CallsService callsService(){
        return new CallsServiceImpl();
    }

    @Bean
    public AmoCRMService amoCRMService(){
        AmoCRMService amoCRMService = new AmoCRMServiceImpl();
        amoCRMService.setUserLogin("elektro-karniz@yandex.ru");
        amoCRMService.setUserHash("e45c8a138cfb6ac531ab61708d2fbb71");
        amoCRMService.setLoginUrl("https://elektrokarniz.amocrm.ru/private/api/auth.php?type=json");
        amoCRMService.setApiBaseUrl("https://elektrokarniz.amocrm.ru/private/api/v2/json/");

        ArrayList<Long> leadClosedStatusesIds = new ArrayList<>();
        leadClosedStatusesIds.add(142l);
        leadClosedStatusesIds.add(143l);

        AmoCRMAPIRequestErrorHandler errorHandler = new AmoCRMAPIRequestErrorHandler();
        amoCRMService.setErrorHandler(errorHandler);
        amoCRMService.setMaxRelogins(5);

        amoCRMService.setLeadClosedStatusesIDs(leadClosedStatusesIds);

        return amoCRMService;
    }

    @Bean
    public IncomingCallBusinessProcess incomingCallBusinessProcess(){
        IncomingCallBusinessProcessImpl incomingCallBusinessProcess = new IncomingCallBusinessProcessImpl();

        incomingCallBusinessProcess.setDefaultUserId(543159l);
        incomingCallBusinessProcess.setPhoneNumberCustomField(561024l);
        incomingCallBusinessProcess.setPhoneNumberCustomFieldLeads(561026l);
        incomingCallBusinessProcess.setMarketingChannelContactsCustomField(561442l);
        incomingCallBusinessProcess.setMarketingChannelLeadsCustomField(561440l);
        incomingCallBusinessProcess.setSourceContactsCustomField(561446l);
        incomingCallBusinessProcess.setSourceLeadsCustomField(561444l);

        HashMap<Integer, Long> projectIdToContactsSource = new HashMap<>();
        projectIdToContactsSource.put(3901, 1324018l);
        projectIdToContactsSource.put(3400, 1324020l);

        HashMap<Integer, Long> projectIdToLeadsSource = new HashMap<>();
        projectIdToLeadsSource.put(3901, 1324014l);
        projectIdToLeadsSource.put(3400, 1324016l);

        incomingCallBusinessProcess.setProjectIdToContactsSource(projectIdToContactsSource);
        incomingCallBusinessProcess.setProjectIdToLeadsSource(projectIdToLeadsSource);

        return incomingCallBusinessProcess;
    }

}
