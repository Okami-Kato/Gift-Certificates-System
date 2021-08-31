package com.epam.esm.dao.config.configurator;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.core.env.Environment;

public abstract class DataSourceConfigurator {
    private final String DRIVER = "driver";
    private final String URL = "url";
    private final String USER = "user";
    private final String PASSWORD = "password";
    private final String MIN_IDLE = "minIdle";
    private final String MAX_IDLE = "maxIdle";
    private final Environment environment;

    DataSourceConfigurator(Environment environment) {
        this.environment = environment;
    }

    public void setUp(BasicDataSource ds) {
        ds.setDriverClassName(environment.getProperty(DRIVER));
        ds.setUrl(environment.getProperty(URL));
        ds.setUsername(environment.getProperty(USER));
        ds.setPassword(environment.getProperty(PASSWORD));
        ds.setMinIdle(environment.getProperty(MIN_IDLE, Integer.TYPE));
        ds.setMaxIdle(environment.getProperty(MAX_IDLE, Integer.TYPE));
    }
}
