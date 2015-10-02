package com.callsintegration;

import com.callsintegration.service.CallTrackingAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * Created by berz on 20.09.2015.
 */


@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
@PropertySource("classpath:application.properties")
public class Application {




    public static void main(String[] args) throws ClassNotFoundException {

        SpringApplication.run(Application.class, args);

        System.out.println("ok");

    }



}
