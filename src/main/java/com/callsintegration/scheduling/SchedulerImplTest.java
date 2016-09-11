package com.callsintegration.scheduling;

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
 * Created by berz on 22.06.2016.
 */
/*@Component*/
public class SchedulerImplTest implements MainScheduler {

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

    @Scheduled(fixedDelay = 30000)
    @Override
    public void newLeadsFromSiteToCRM(){
        /*JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());


        try {
            jobLauncher.run(newLeadsFromSiteToCRMJob, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }*/
    }

    @Scheduled(fixedDelay = 30000)
    @Override
    public void callsImportProcess() {
        runCallsImport();
    }

    @Override
    public void callsImportProcessMedium() {

    }

    @Override
    public void callsImportProcessRarely() {

    }

    @Override
    @Scheduled(fixedDelay = 30000)
    public void callsToCRM() {
        runImportCallsToCRM();
    }

    @Override
    public void newLeads() {

    }

    private void runImportCallsToCRM(){


        try {

            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addDate("start", new Date());
            jobLauncher.run(newCallsToCRMJob, jobParametersBuilder.toJobParameters());

            System.out.println("START calls to CRM job!");
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    public void runCallsImport(){

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());

        System.out.println("START calls import job!");

        try {
            jobLauncher.run(callsImportJob, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    public int hourOfDay(){
        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return hour;
    }
}
