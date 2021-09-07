package com.epam.esm.dao.config.configurator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@PropertySource("classpath:database-dev.properties")
public class DevDataSourceConfigurator extends DataSourceConfigurator {
    @Autowired
    public DevDataSourceConfigurator(Environment environment) {
        super(environment);
    }
}
