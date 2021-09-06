package com.epam.esm.dao.config.configurator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@PropertySource({"classpath:database-prod.properties", "classpath:database-credentials-prod.properties"})
public class ProdDataSourceConfigurator extends DataSourceConfigurator {
    @Autowired
    ProdDataSourceConfigurator(Environment environment) {
        super(environment);
    }
}
