package com.callsintegration.settings;

/**
 * Created by berz on 22.07.2016.
 */
public class APISettings {

    /*
    *
    * AmoCRM
     */
    public static String AmoCRMUser = "tsvetkov.ir@yandex.ru";
    public static String AmoCRMHash = "f23f18376f4b9652830f72e981268be4";
    public static String AmoCRMLoginUrl = "https://elkarniz.amocrm.ru/private/api/auth.php?type=json";
    public static String AmoCRMApiBaseUrl = "https://elkarniz.amocrm.ru/private/api/v2/json/";

    public static Long[] AmoCRMLeadClosedStatuses = {142l, 143l};
    public static int AmoCRMMaxRelogins = 5;
    // Пользователь по умолчанию
    public static Long AmoCRMDefaultUserID = 1110783l;
    // Кастомное поле "номер телефона" для контакта
    public static Long AmoCRMPhoneNumberCustomField = 1399410l;
    // Кастомное поле "номер телефона" для сделки
    public static Long AmoCRMPhoneNumberCustomFieldLeads = 1399360l;
    // Встроенное поле "номер телефона для контакта"
    public static Long AmoCRMPhoneNumberStockFieldContact = 1369748l;
    // enum значение "Рабочий" для встроенного поля "номер телефона"
    public static String AmoCRMPhoneNumberStockFieldContactEnumWork = "3282364";
    // кастомное поле "Рекламный канал" для контакта
    public static Long AmoCRMMarketingChannelContactsCustomField = 1399424l;
    // кастомное поле "Рекламный канал" для сделки
    public static Long AmoCRMMarketingChannelLeadsCustomField = 1399398l;
    // кастомное поле "Источник" (сайт) для контакта
    public static Long AmoCRMSourceContactsCustomField = 1399786l;
    // встроенное поле "email" для контакта
    public static Long AmoCRMEmailContactCustomField = 1369750l;
    // кастомное поле "Источник" (сайт) для сделки
    public static Long AmoCRMSourceLeadsCustomField = 1399784l;
    // кастомное поле "Комментарий" для сделки
    public static Long AMOCRMLeadCommentField = 1399400l;
    // enum значение "email" для контакта
    public static String AmoCRMEmailContactEnum = "3282376";
    // кастомное поле "utm_source"
    // todo
    public static Long AmoCRMUtmSourceCustomFieldId = 568306l;
    // кастомное поле "utm_medium"
    // todo
    public static Long AmoCRMUtmMediumCustomFieldId = 568308l;
    // кастомное поле "utm_campaign"
    // todo
    public static Long AmoCRMUtmCampaignCustomFieldId = 568310l;

    // Тег "заявка с сайта"
    public static Long AMOCRMLeadFromSiteTagId = 98794l;

    // todo - разобраться, нужны ли все еще эти поля
    public static Long AmoCRMNewLeadFromSiteStatusCustomFieldId = 576380l;
    public static Long AmoCRMNewLeadFromSiteStatusCustomFieldEnumNotProcessed = 1353964l;

    /*
    *
    * Calltracking
    */
    public static String CallTrackingLogin = "info@elektro-karniz.ru";
    public static String CallTrackingPassword = "call456KL";
    public static String CallTrackingWebLogin = "info@elektro-karniz.ru";
    public static String CallTrackingWebPassword = "call456KL";
    public static String CallTrackingLoginUrl = "https://calltracking.ru/admin/login";
    public static String CallTrackingAPIUrl = "https://calltracking.ru/api/get_data.php";
    public static String CallTrackingAPILoginUrl = "https://calltracking.ru/api/login.php";

    public static Integer[] CallTrackingProjects = {4803, 4837, 4831};

}
