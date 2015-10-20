package com.callsintegration.scheduling;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by berz on 20.09.2015.
 */
public interface MainScheduler {
    @Scheduled(fixedDelay = 120000)
    void callsImportProcess();

    @Scheduled(fixedDelay = 30000)
    void callsToCRM();
}
