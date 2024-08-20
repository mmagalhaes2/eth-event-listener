

package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
public class MongoConfiguration {

//    @Bean
//    public MongoClientOptions mongoClientOptions() {
//        return MongoClientOptions.builder()
//                .connectionsPerHost(1000)
//                .build();
//    }
}
