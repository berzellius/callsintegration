package com.callsintegration.config;


import com.callsintegration.dto.api.ErrorHandlers.AmoCRMAPIRequestErrorHandler;
import com.callsintegration.dto.api.ErrorHandlers.CalltrackingAPIRequestErrorHandler;
import com.callsintegration.interceptors.AddTemplatesDataInterceptor;
import com.callsintegration.service.*;
import com.callsintegration.settings.APISettings;
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
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.*;

/**
 * Created by berz on 20.10.14.
 */
@Configuration
public class ServiceBeanConfiguration {

    /*HashMap<Integer, Long> projectIdToLeadsSource() {
        HashMap<Integer, Long> projectIdToLeadsSource = new HashMap<>();
        projectIdToLeadsSource.put(3901, 1324014l);
        projectIdToLeadsSource.put(3400, 1324016l);
        projectIdToLeadsSource.put(4318, 1337536l);
        projectIdToLeadsSource.put(4319, 1335876l);

        return projectIdToLeadsSource;
    }*/

    Long sourceLeadsCustomField(){
        return APISettings.AmoCRMSourceLeadsCustomField;
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
        callTrackingAPIService.setApiURL(APISettings.CallTrackingAPIUrl);

        callTrackingAPIService.setLoginURL(APISettings.CallTrackingAPILoginUrl);
        callTrackingAPIService.setLoginMethod(HttpMethod.POST);

        callTrackingAPIService.setLogin(APISettings.CallTrackingLogin);
        callTrackingAPIService.setPassword(APISettings.CallTrackingPassword);
        callTrackingAPIService.setWebSiteLogin(APISettings.CallTrackingWebLogin);
        callTrackingAPIService.setWebSitePassword(APISettings.CallTrackingWebPassword);
        callTrackingAPIService.setWebSiteLoginUrl(APISettings.CallTrackingLoginUrl);
        callTrackingAPIService.setProjects(APISettings.CallTrackingProjects);

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
        amoCRMService.setUserLogin(APISettings.AmoCRMUser);
        amoCRMService.setUserHash(APISettings.AmoCRMHash);
        amoCRMService.setLoginUrl(APISettings.AmoCRMLoginUrl);
        amoCRMService.setApiBaseUrl(APISettings.AmoCRMApiBaseUrl);

        ArrayList<Long> leadClosedStatusesIds = new ArrayList<>(Arrays.asList(APISettings.AmoCRMLeadClosedStatuses));
        amoCRMService.setLeadClosedStatusesIDs(leadClosedStatusesIds);

        AmoCRMAPIRequestErrorHandler errorHandler = new AmoCRMAPIRequestErrorHandler();
        amoCRMService.setErrorHandler(errorHandler);
        amoCRMService.setMaxRelogins(APISettings.AmoCRMMaxRelogins);

        return amoCRMService;
    }

    @Bean
    public IncomingCallBusinessProcess incomingCallBusinessProcess(){
        IncomingCallBusinessProcessImpl incomingCallBusinessProcess = new IncomingCallBusinessProcessImpl();
        incomingCallBusinessProcess.setDefaultUserId(APISettings.AmoCRMDefaultUserID);
        incomingCallBusinessProcess.setPhoneNumberCustomField(APISettings.AmoCRMPhoneNumberCustomField);
        incomingCallBusinessProcess.setPhoneNumberCustomFieldLeads(APISettings.AmoCRMPhoneNumberCustomFieldLeads);
        incomingCallBusinessProcess.setMarketingChannelContactsCustomField(APISettings.AmoCRMMarketingChannelContactsCustomField);
        incomingCallBusinessProcess.setMarketingChannelLeadsCustomField(APISettings.AmoCRMMarketingChannelLeadsCustomField);
        incomingCallBusinessProcess.setSourceContactsCustomField(APISettings.AmoCRMSourceContactsCustomField);
        incomingCallBusinessProcess.setEmailContactCustomField(APISettings.AmoCRMEmailContactCustomField);
        incomingCallBusinessProcess.setEmailContactEnum(APISettings.AmoCRMEmailContactEnum);
        incomingCallBusinessProcess.setSourceLeadsCustomField(sourceLeadsCustomField());

        /*HashMap<Integer, Long> projectIdToContactsSource = new HashMap<>();
        projectIdToContactsSource.put(3901, 1324018l);
        projectIdToContactsSource.put(3400, 1324020l);
        projectIdToContactsSource.put(4318, 1338944l);
        projectIdToContactsSource.put(4319, 1335878l);*/

        //HashMap<Integer, Long> projectIdToLeadsSource = projectIdToLeadsSource();

        //incomingCallBusinessProcess.setProjectIdToContactsSource(projectIdToContactsSource);
        //incomingCallBusinessProcess.setProjectIdToLeadsSource(projectIdToLeadsSource);

        return incomingCallBusinessProcess;
    }

    @Bean
    public AddingCallNotesToEmptyLead addingCallNotesToEmptyLead(){
        AddingCallNotesToEmptyLead addingCallNotesToEmptyLead = new AddingCallNotesToEmptyLeadImpl();
        addingCallNotesToEmptyLead.setPhoneNumberCustomFieldLeads(APISettings.AmoCRMPhoneNumberCustomFieldLeads);

        return addingCallNotesToEmptyLead;
    }

    @Bean
    public CallTrackingSourceConditionService callTrackingSourceConditionService(){
        return new CallTrackingSourceConditionServiceImpl();
    }

    @Bean
    public AmoCRMLeadsFromSiteService amoCRMLeadsFromSiteService(){
        AmoCRMLeadsFromSiteService amoCRMLeadsFromSiteService = new AmoCRMLeadsFromSiteServiceImpl();
        amoCRMLeadsFromSiteService.setMarketingChannelCustomFieldId(APISettings.AmoCRMMarketingChannelLeadsCustomField);
        amoCRMLeadsFromSiteService.setUtmSourceCustomFieldId(APISettings.AmoCRMUtmSourceCustomFieldId);
        amoCRMLeadsFromSiteService.setUtmMediumCustomFieldId(APISettings.AmoCRMUtmMediumCustomFieldId);
        amoCRMLeadsFromSiteService.setUtmCampaignCustomFieldId(APISettings.AmoCRMUtmCampaignCustomFieldId);
        amoCRMLeadsFromSiteService.setNewLeadFromSiteStatusCustomFieldId(APISettings.AmoCRMNewLeadFromSiteStatusCustomFieldId);
        amoCRMLeadsFromSiteService.setNewLeadFromSiteStatusCustomFieldEnumNotProcessed(APISettings.AmoCRMNewLeadFromSiteStatusCustomFieldEnumNotProcessed);
        amoCRMLeadsFromSiteService.setDefaultUserID(APISettings.AmoCRMDefaultUserID);
        amoCRMLeadsFromSiteService.setMarketingChannelContactsCustomField(APISettings.AmoCRMMarketingChannelContactsCustomField);
        amoCRMLeadsFromSiteService.setMarketingChannelLeadsCustomField(APISettings.AmoCRMMarketingChannelLeadsCustomField);
        amoCRMLeadsFromSiteService.setPhoneNumberCustomField(APISettings.AmoCRMPhoneNumberCustomField);
        amoCRMLeadsFromSiteService.setPhoneNumberCustomFieldLeads(APISettings.AmoCRMPhoneNumberCustomFieldLeads);
        amoCRMLeadsFromSiteService.setEmailContactCustomField(APISettings.AmoCRMEmailContactCustomField);
        amoCRMLeadsFromSiteService.setSourceContactsCustomField(APISettings.AmoCRMSourceContactsCustomField);
        amoCRMLeadsFromSiteService.setSourceLeadsCustomField(sourceLeadsCustomField());
        amoCRMLeadsFromSiteService.setEmailContactEnum(APISettings.AmoCRMEmailContactEnum);
        amoCRMLeadsFromSiteService.setPhoneNumberContactStockField(APISettings.AmoCRMPhoneNumberStockFieldContact);
        amoCRMLeadsFromSiteService.setPhoneNumberStockFieldContactEnumWork(APISettings.AmoCRMPhoneNumberStockFieldContactEnumWork);
       // amoCRMLeadsFromSiteService.setProjectIdToLeadsSource(projectIdToLeadsSource());


        return amoCRMLeadsFromSiteService;
    }
}
