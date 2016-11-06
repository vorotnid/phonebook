package com.phonebook.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@ComponentScan
public class DataSourceConfig {

    @Autowired
    Environment env;

    @Bean
    public DataSource configureDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getRequiredProperty("datasource.url"));
        ds.setDriverClassName(env.getRequiredProperty("datasource.driver"));
        ds.setUsername(env.getRequiredProperty("datasource.username"));
        ds.setPassword(env.getRequiredProperty("datasource.password"));
        ds.setInitialSize(Integer.valueOf(env.getRequiredProperty("datasource.initialSize")));
        ds.setMinIdle(Integer.valueOf(env.getRequiredProperty("datasource.minIdle")));
        ds.setMaxIdle(Integer.valueOf(env.getRequiredProperty("datasource.maxIdle")));
        ds.setTimeBetweenEvictionRunsMillis(Integer.valueOf(env.getRequiredProperty("datasource.timeBetweenEvictionMillis")));
        ds.setMinEvictableIdleTimeMillis(Integer.valueOf(env.getRequiredProperty("datasource.evictableIdleTimeMillis")));
        ds.setTestOnBorrow(Boolean.valueOf(env.getRequiredProperty("datasource.testOnBorrow")));
        ds.setValidationQuery(env.getRequiredProperty("datasource.validationQuery"));
        return ds;
    }

    @Profile("fileStorage")
    @Bean
    public Gson gson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
