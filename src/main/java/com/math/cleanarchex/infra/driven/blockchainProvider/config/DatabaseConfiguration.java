package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@Import({DatabaseConfiguration.WithMongo.class})
public class DatabaseConfiguration {

    @ConditionalOnProperty(name = "database.type", havingValue = "MONGO")
    @EnableAutoConfiguration(
            exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
                    HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
    @EnableMongoRepositories(basePackages = {BaseConfiguration.BASE_PACKAGE})
    static class WithMongo {

    }

}
