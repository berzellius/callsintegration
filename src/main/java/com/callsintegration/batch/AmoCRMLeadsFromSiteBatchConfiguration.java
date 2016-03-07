package com.callsintegration.batch;

import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.callsintegration.reader.AmoCRMLeadsQueryReader;
import com.callsintegration.reader.ResetableItemReader;
import com.callsintegration.service.AmoCRMLeadsFromSiteService;
import com.callsintegration.service.AmoCRMService;
import com.callsintegration.writer.AmoCRMUpdatedLeadsWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.batch.core.Step;

import java.text.ParseException;
import java.util.List;

/**
 * Created by berz on 06.03.2016.
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@PropertySource("classpath:batch.properties")
public class AmoCRMLeadsFromSiteBatchConfiguration {

    static String newLeadsFromSiteTag = "FROM_SITE_NOT_PROCESSED";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private AmoCRMLeadsFromSiteService amoCRMLeadsFromSiteService;

    @Bean
    public ResetableItemReader<List<AmoCRMLead>> leadsReader() throws ParseException{
        AmoCRMLeadsQueryReader reader = new AmoCRMLeadsQueryReader(newLeadsFromSiteTag);
        return reader;
    }

    @Bean
    public ItemWriter<List<AmoCRMLead>> leadsWriter(){
        return new AmoCRMUpdatedLeadsWriter();
    }

    @Bean
    public ItemProcessor<List<AmoCRMLead>, List<AmoCRMLead>> leadsProcessor(){
        return new ItemProcessor<List<AmoCRMLead>, List<AmoCRMLead>>() {
            @Override
            public List<AmoCRMLead> process(List<AmoCRMLead> amoCRMLeads) throws Exception {
                for(AmoCRMLead amoCRMLead : amoCRMLeads){
                    amoCRMLeadsFromSiteService.processLead(amoCRMLead);
                }

                return amoCRMLeads;
            }
        };
    }

    @Bean
    public Step newLeadsStep(
            StepBuilderFactory stepBuilderFactory,
            ResetableItemReader<List<AmoCRMLead>> leadsReader,
            ItemProcessor<List<AmoCRMLead>, List<AmoCRMLead>> leadsProcessor,
            ItemWriter<List<AmoCRMLead>> leadsWriter
    ){
        return stepBuilderFactory.get("newLeadsStep")
                .<List<AmoCRMLead>, List<AmoCRMLead>>chunk(3)
                .reader(leadsReader)
                .processor(leadsProcessor)
                .writer(leadsWriter)
                .build();

    }

    @Bean
    public Job leadsFromSiteJob(Step newLeadsStep){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("leadsFromSiteJob")
                .incrementer(runIdIncrementer)
                .flow(newLeadsStep)
                .end()
                .build();
    }
}
