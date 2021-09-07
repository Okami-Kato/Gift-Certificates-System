package com.epam.esm.dao.config.configurator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@PropertySource("classpath:database-test.properties")
public class TestDataSourceConfigurator extends DataSourceConfigurator{
    @Autowired
    TestDataSourceConfigurator(Environment environment) {
        super(environment);
    }
}
