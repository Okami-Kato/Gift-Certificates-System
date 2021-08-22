package com.epam.esm.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan("com.epam.esm.dao")
@EnableTransactionManagement
public class SpringDAOTestConfig {
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
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}