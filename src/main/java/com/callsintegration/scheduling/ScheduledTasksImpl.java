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
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by berz on 28.09.2016.
 */
@Service
public class ScheduledTasksImpl implements ScheduledTasks {
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

    @Override
    public void newLeadsFromSiteToCRM(){
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());


        try {
            callTrackingAPIService.updateMarketingChannelsFromCalltracking();

            jobLauncher.run(newLeadsFromSiteToCRMJob, jobParametersBuilder.toJobParameters());
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

    @Override
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

    @Override
    public void runImportCallsToCRM(){


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

}
