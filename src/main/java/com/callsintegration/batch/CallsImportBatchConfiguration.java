package com.callsintegration.batch;

import com.callsintegration.dmodel.Call;
import com.callsintegration.reader.CallTrackingCallsReader;
import com.callsintegration.repository.CallRepository;
import com.callsintegration.service.CallTrackingAPIService;
import com.callsintegration.service.CallsService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.batch.item.ItemProcessor;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by berz on 20.09.2015.
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@PropertySource("classpath:batch.properties")
public class CallsImportBatchConfiguration {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private CallRepository callRepository;

    @Autowired
    private CallTrackingAPIService callTrackingAPIService;

    @Bean
    public ItemReader<List<Call>> callsReader() throws ParseException {

        // Внимание: один шаг чтения должен сопровождаться одним шагом записи в БД. иначе будут засасываться повторяющиеся данные.
        CallTrackingCallsReader reader = new CallTrackingCallsReader(new Date(), new Date(), 100, CallTrackingCallsReader.DateMode.UPDATE_EACH_READ);
        return reader;
    }

    @Bean
    public ItemReader<List<Call>> callsReaderCustom() throws ParseException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.YEAR, 2016);
        c1.set(Calendar.MONTH, Calendar.OCTOBER);
        c1.set(Calendar.DAY_OF_MONTH, 7);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);

        c2.set(Calendar.YEAR, 2016);
        c2.set(Calendar.MONTH, Calendar.OCTOBER);
        c2.set(Calendar.DAY_OF_MONTH, 14);
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);


        Date dt1 = c1.getTime();
        Date dt2 = c2.getTime();
        System.out.println("f: " + dt1 + ", t: " + dt2);

        CallTrackingCallsReader readerCustom = new CallTrackingCallsReader(dt1, dt2, 100, CallTrackingCallsReader.DateMode.NO_UPDATE);
        return readerCustom;
    }

    @Bean
    public ItemProcessor<List<Call>, List<Call>> itemProcessor(){
        return new ItemProcessor<List<Call>, List<Call>>() {
            @Override
            public List<Call> process(List<Call> calls) throws Exception {

                System.out.println("process calls: " + calls);

                for(Call call : calls){
                    callTrackingAPIService.processCallOnImport(call);
                }

                return calls;
            }
        };
    }

    @Bean
    public ItemWriter<List<Call>> writer(){

        return new ItemWriter<List<Call>>() {
            @Override
            public void write(List<? extends List<Call>> callsPortions) throws Exception {
                for(List<Call> calls : callsPortions) {
                    System.out.println("write calls to base: " + calls);
                    callRepository.save(calls);
                }
            }
        };
    }

    @Bean
    public Step callsImportStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<List<Call>> callsReader,
            ItemReader<List<Call>> callsReaderCustom,
            ItemProcessor<List<Call>, List<Call>> itemProcessor,
            ItemWriter<List<Call>> writer
    ){
       return stepBuilderFactory.get("callsImportStep")
               // представляется верным, что chunk size - это эквивалент commit interval
                .<List<Call>, List<Call>>chunk(1)
                //.reader(callsReaderCustom)
                .reader(callsReader)
                .processor(itemProcessor)
                .writer(writer)
                /*.faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(2000)*/
                .build();
    }

    @Bean
    public Job callsImportJob(Step callsImportStep){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("callsImportJob")
                .incrementer(runIdIncrementer)
                .flow(callsImportStep)
                .end()
                .build();
    }

}
