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

    HashMap<Integer, Long> projectIdToLeadsSource() {
        HashMap<Integer, Long> projectIdToLeadsSource = new HashMap<>();
        projectIdToLeadsSource.put(3901, 1324014l);
        projectIdToLeadsSource.put(3400, 1324016l);
        projectIdToLeadsSource.put(4318, 1337536l);
        projectIdToLeadsSource.put(4319, 1335876l);

        return projectIdToLeadsSource;
    }

    Long sourceLeadsCustomField(){
        return 561444l;
    }

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
        //return new LocalProjectSettings();
        // Боевой сервер
        return new RemoteProjectSettings();
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
        callTrackingAPIService.setWebSiteLogin("info@home-motion.ru");
        callTrackingAPIService.setWebSitePassword("SsX0d1XE75");
        callTrackingAPIService.setWebSiteLoginUrl("https://calltracking.ru/admin/login");
        Integer[] projects = {3901, 3400, 4318, 4319};
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
        incomingCallBusinessProcess.setEmailContactCustomField(459344l);
        incomingCallBusinessProcess.setEmailContactEnum("1084672");
        incomingCallBusinessProcess.setSourceLeadsCustomField(sourceLeadsCustomField());

        HashMap<Integer, Long> projectIdToContactsSource = new HashMap<>();
        projectIdToContactsSource.put(3901, 1324018l);
        projectIdToContactsSource.put(3400, 1324020l);
        projectIdToContactsSource.put(4318, 1338944l);
        projectIdToContactsSource.put(4319, 1335878l);

        HashMap<Integer, Long> projectIdToLeadsSource = projectIdToLeadsSource();

        incomingCallBusinessProcess.setProjectIdToContactsSource(projectIdToContactsSource);
        incomingCallBusinessProcess.setProjectIdToLeadsSource(projectIdToLeadsSource);

        return incomingCallBusinessProcess;
    }

    @Bean
    public AddingCallNotesToEmptyLead addingCallNotesToEmptyLead(){
        AddingCallNotesToEmptyLead addingCallNotesToEmptyLead = new AddingCallNotesToEmptyLeadImpl();
        addingCallNotesToEmptyLead.setPhoneNumberCustomFieldLeads(561026l);

        return addingCallNotesToEmptyLead;
    }

    @Bean
    public CallTrackingSourceConditionService callTrackingSourceConditionService(){
        return new CallTrackingSourceConditionServiceImpl();
    }

    @Bean
    public AmoCRMLeadsFromSiteService amoCRMLeadsFromSiteService(){
        AmoCRMLeadsFromSiteService amoCRMLeadsFromSiteService = new AmoCRMLeadsFromSiteServiceImpl();
        amoCRMLeadsFromSiteService.setMarketingChannelCustomFieldId(561440l);
        amoCRMLeadsFromSiteService.setUtmSourceCustomFieldId(568306l);
        amoCRMLeadsFromSiteService.setUtmMediumCustomFieldId(568308l);
        amoCRMLeadsFromSiteService.setUtmCampaignCustomFieldId(568310l);
        amoCRMLeadsFromSiteService.setNewLeadFromSiteStatusCustomFieldId(576380l);
        amoCRMLeadsFromSiteService.setNewLeadFromSiteStatusCustomFieldEnumNotProcessed(1353964l);
        amoCRMLeadsFromSiteService.setProjectIdToLeadsSource(projectIdToLeadsSource());
        amoCRMLeadsFromSiteService.setSourceLeadsCustomField(sourceLeadsCustomField());

        return amoCRMLeadsFromSiteService;
    }
}
