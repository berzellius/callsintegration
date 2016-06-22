package com.callsintegration.scheduling;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by berz on 20.09.2015.
 */
public interface MainScheduler {

    @Scheduled(fixedDelay = 30000)
    void newLeadsFromSiteToCRM();

    @Scheduled(fixedDelay = 120000)
    void callsImportProcess();

    @Scheduled(fixedDelay = 180000)
    void callsImportProcessMedium();

    /*
     * 450 сек = 7 минут 30 секунд
     */
    @Scheduled(fixedDelay = 450000)
    void callsImportProcessRarely();

    @Scheduled(fixedDelay = 30000)
    void callsToCRM();

    @Scheduled(fixedDelay = 60000)
    void newLeads();
}
