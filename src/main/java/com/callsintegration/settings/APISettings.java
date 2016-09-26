package com.callsintegration.settings;

/**
 * Created by berz on 22.07.2016.
 */
public class APISettings {

    /*
    *
    * AmoCRM
     */
    public static String AmoCRMUser = "elektro-karniz@yandex.ru";
    public static String AmoCRMHash = "ca70636c49a9e1d747022e47d546d2ab";
    public static String AmoCRMLoginUrl = "https://elektrokarniz.amocrm.ru/private/api/auth.php?type=json";
    public static String AmoCRMApiBaseUrl = "https://elektrokarniz.amocrm.ru/private/api/v2/json/";

    public static Long[] AmoCRMLeadClosedStatuses = {142l, 143l};
    public static int AmoCRMMaxRelogins = 5;
    public static Long AmoCRMDefaultUserID = 543159l;
    public static Long AmoCRMPhoneNumberCustomField = 561024l;
    public static Long AmoCRMPhoneNumberCustomFieldLeads = 561026l;
    public static Long AmoCRMPhoneNumberStockFieldContact = 459342l;
    public static String AmoCRMPhoneNumberStockFieldContactEnumWork = "1084660";
    public static Long AmoCRMMarketingChannelContactsCustomField = 561442l;
    public static Long AmoCRMMarketingChannelLeadsCustomField = 561440l;
    public static Long AmoCRMSourceContactsCustomField = 561446l;
    public static Long AmoCRMEmailContactCustomField = 459344l;
    public static Long AmoCRMSourceLeadsCustomField = 561444l;
    public static String AmoCRMEmailContactEnum = "1084672";
    public static Long AmoCRMUtmSourceCustomFieldId = 568306l;
    public static Long AmoCRMUtmMediumCustomFieldId = 568308l;
    public static Long AmoCRMUtmCampaignCustomFieldId = 568310l;
    public static Long AmoCRMNewLeadFromSiteStatusCustomFieldId = 576380l;
    public static Long AmoCRMNewLeadFromSiteStatusCustomFieldEnumNotProcessed = 1353964l;

    /*
    *
    * Calltracking
     */
    public static String CallTrackingLogin = "info@home-motion.ru";
    public static String CallTrackingPassword = "SsX0d1XE75";
    public static String CallTrackingWebLogin = "info@home-motion.ru";
    public static String CallTrackingWebPassword = "SsX0d1XE75";
    public static String CallTrackingLoginUrl = "https://calltracking.ru/admin/login";
    public static String CallTrackingAPIUrl = "https://calltracking.ru/api/get_data.php";
    public static String CallTrackingAPILoginUrl = "https://calltracking.ru/api/login.php";

    public static Integer[] CallTrackingProjects = {3901, 3400, 4318, 4319, 4590, 4539};
}
