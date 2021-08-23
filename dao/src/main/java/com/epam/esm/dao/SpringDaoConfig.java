package com.epam.esm.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan("com.epam.esm.dao")
public class SpringDaoConfig {
    @Autowired
    Environment environment;

    private final String DRIVER = "driver";
    private final String URL = "url";
    private final String USER = "user";
    private final String PASSWORD = "password";
    private final String MIN_IDLE = "minIdle";
    private final String MAX_IDLE = "maxIdle";

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(environment.getProperty(DRIVER));
        ds.setUrl(environment.getProperty(URL));
        ds.setUsername(environment.getProperty(USER));
        ds.setPassword(environment.getProperty(PASSWORD));
        ds.setMinIdle(environment.getProperty(MIN_IDLE, Integer.TYPE));
        ds.setMaxIdle(environment.getProperty(MAX_IDLE, Integer.TYPE));
        return ds;
    }

    @Bean
    @Scope("prototype")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}