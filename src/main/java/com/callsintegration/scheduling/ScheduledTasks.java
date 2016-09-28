package com.callsintegration.scheduling;

import org.springframework.stereotype.Service;

/**
 * Created by berz on 28.09.2016.
 */
@Service
public interface ScheduledTasks {
    void newLeadsFromSiteToCRM();

    void runCallsImport();

    void runImportCallsToCRM();
}
