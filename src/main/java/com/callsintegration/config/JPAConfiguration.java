package com.callsintegration.config;

import com.callsintegration.settings.ProjectSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by berz on 20.10.14.
 */
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class JPAConfiguration {

    @Autowired
    ProjectSettings projectSettings;


    @Bean
    public DataSource dataSource() throws SQLException {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(
                projectSettings.getDatabaseConnectionConfig().get("path"),
                projectSettings.getDatabaseConnectionConfig().get("database"),
                projectSettings.getDatabaseConnectionConfig().get("password")
        );


        driverManagerDataSource.setDriverClassName("org.postgresql.Driver");

        return driverManagerDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.callsintegration.dmodel"});

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }

    @Bean
    public ViewResolver getViewResolver(){
        ResourceBundleViewResolver resolver = new ResourceBundleViewResolver();
        resolver.setBasename("views");
        resolver.setOrder(1);
        return resolver;
    }
}