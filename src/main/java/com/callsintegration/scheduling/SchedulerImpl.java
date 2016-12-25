package com.callsintegration.scheduling;

import com.callsintegration.exception.APIAuthException;
import com.callsintegration.service.AddingCallNotesToEmptyLead;
import com.callsintegration.service.CallTrackingAPIService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by berz on 20.09.2015.
 */
@Component
public class SchedulerImpl implements MainScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job callsImportJob;

    @Autowired
    Job newCallsToCRMJob;

    @Autowired
    Job leadsFromSiteJob;

    @Autowired
    Job newLeadsFromSiteToCRMJob;

    @Autowired
    AddingCallNotesToEmptyLead addingCallNotesToEmptyLead;

    @Autowired
    CallTrackingAPIService callTrackingAPIService;

    @Autowired
    ScheduledTasks scheduledTasks;

    @Scheduled(fixedDelay = 120000)
    @Override
    public void newLeadsFromSiteToCRM(){
        scheduledTasks.newLeadsFromSiteToCRM();
    }



    public void runCallsImport(){
        scheduledTasks.runCallsImport();
    }

    public int hourOfDay(){
        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return hour;
    }

    /*
         * 144 сек
         */
    @Scheduled(fixedDelay = 144000)
    @Override
    public void callsImportProcess(){
        int hour = hourOfDay();

        if(hour >= 10 && hour < 22) {
            runCallsImport();
        }
    }

    /*
     * 1500 сек = 25 минут
     */
    @Scheduled(fixedDelay = 3600000)
    @Override
    public void callsImportProcessMedium(){
        int hour = hourOfDay();

        if((hour >= 22 && hour < 24) || (hour >= 6 && hour < 10)) {
            runCallsImport();
        }
    }

    /*
     * 3600 сек = 60 минут
     */
    @Scheduled(fixedDelay = 3600000)
    @Override
    public void callsImportProcessRarely(){
        int hour = hourOfDay();

        if((hour >= 0 && hour < 8)) {
            runCallsImport();
        }
    }


    /*
    *
    * 60 сек
     */
    @Scheduled(fixedDelay = 120000)
    @Override
    public void callsToCRM(){
        //int hour = hourOfDay();

        //if(hour >= 22 || hour <= 8) {
            runImportCallsToCRM();
        //}
    }


    @Scheduled(fixedDelay = 600000)
    @Override
    public void newLeads(){

        int hour = hourOfDay();

        if((hour >= 3 && hour <= 4) || (hour >= 22 && hour <= 23))
        runNewAmoCRMLeadsFromSiteProcessing();
    }

    private void runNewAmoCRMLeadsFromSiteProcessing(){
        try {
            callTrackingAPIService.updateMarketingChannelsFromCalltracking();

            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addDate("start", new Date());
            jobLauncher.run(leadsFromSiteJob, jobParametersBuilder.toJobParameters());

            System.out.println("New leads from site processing started!");
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (APIAuthException e) {
            e.printStackTrace();
        }
    }

    private void runImportCallsToCRM(){
        scheduledTasks.runImportCallsToCRM();
    }


}
