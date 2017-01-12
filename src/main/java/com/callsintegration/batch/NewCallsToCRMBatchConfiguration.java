package com.callsintegration.batch;

import com.callsintegration.dmodel.Call;
import com.callsintegration.service.AmoCRMService;
import com.callsintegration.service.CallTrackingAPIService;
import com.callsintegration.businessprocesses.processes.IncomingCallBusinessProcess;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by berz on 14.10.2015.
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@PropertySource("classpath:batch.properties")
public class NewCallsToCRMBatchConfiguration {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    IncomingCallBusinessProcess incomingCallBusinessProcess;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    CallTrackingAPIService callTrackingAPIService;

    @Autowired
    AmoCRMService amoCRMService;

    @Bean
    public ItemReader<Call> callReader(){
        JpaPagingItemReader<Call> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        reader.setQueryString("select c from Call c where state = :st");
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("st", Call.State.NEW);
        reader.setParameterValues(params);

        return reader;
    }

    @Bean
    public ItemProcessor<Call, Call> callProcessor(){
        return new ItemProcessor<Call, Call>() {
            @Override
            public Call process(Call call) throws Exception {
                incomingCallBusinessProcess.newIncomingCall(call);
                return call;
            }
        };
    }

    @Bean
    public Step callAddToCRMStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<Call> callItemReader,
            ItemProcessor<Call, Call> callProcessor

    ){
        return stepBuilderFactory.get("callAddToCRMStep")
                .<Call, Call>chunk(1)
                .reader(callItemReader)
                .processor(callProcessor)
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(2000)
                .build();
    }

    @Bean
    public Job newCallsToCRMJob(
        Step callAddToCRMStep
    ){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("newCallsToCRMJob")
                .incrementer(runIdIncrementer)
                .flow(callAddToCRMStep)
                .end()
                .build();
    }
}
