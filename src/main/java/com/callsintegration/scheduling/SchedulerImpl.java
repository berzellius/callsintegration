package com.callsintegration.scheduling;

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

    @Scheduled(fixedDelay = 60000)
    @Override
    public void callsImportProcess(){
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

    @Scheduled(fixedDelay = 30000)
    @Override
    public void callsToCRM(){
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());

        System.out.println("START calls to CRM job!");

        try {
            jobLauncher.run(newCallsToCRMJob, jobParametersBuilder.toJobParameters());
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
