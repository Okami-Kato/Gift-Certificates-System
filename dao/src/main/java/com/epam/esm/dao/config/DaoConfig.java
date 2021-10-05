package com.epam.esm.dao.config;

import com.epam.esm.dao.config.configurator.DataSourceConfigurator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.epam.esm.dao.impl", "com.epam.esm.dao.config.configurator"})
public class DaoConfig {
    private final DataSourceConfigurator dataSourceConfigurator;

    @Autowired
    public DaoConfig(DataSourceConfigurator dataSourceConfigurator) {
        this.dataSourceConfigurator = dataSourceConfigurator;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        dataSourceConfigurator.setUp(ds);
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Autowired
    @Profile("test")
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}