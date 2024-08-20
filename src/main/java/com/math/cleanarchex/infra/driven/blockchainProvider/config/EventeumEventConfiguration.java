package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.internal.DoNothingEventeumEventBroadcaster;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.internal.EventeumEventBroadcaster;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class EventeumEventConfiguration {

//    @Bean
//    @ConditionalOnProperty(name = "broadcaster.multiInstance", havingValue = "true")
//    public EventeumEventBroadcaster kafkaFilterEventBroadcaster(KafkaTemplate<String, EventeumMessage> kafkaTemplate,
//                                                                KafkaSettings kafkaSettings) {
//        return new KafkaEventeumEventBroadcaster(kafkaTemplate, kafkaSettings);
//    }
//
//    @Bean
//    @ConditionalOnProperty(name = "broadcaster.multiInstance", havingValue = "true")
//    public EventeumInternalEventConsumer kafkaFilterEventConsumer(SubscriptionService subscriptionService,
//                                                                  TransactionMonitoringService transactionMonitoringService,
//                                                                  KafkaSettings kafkaSettings) {
//        return new KafkaFilterEventConsumer(subscriptionService, transactionMonitoringService, kafkaSettings);
//    }

    @Bean
    @ConditionalOnProperty(name = "broadcaster.multiInstance", havingValue = "false")
    public EventeumEventBroadcaster doNothingFilterEventBroadcaster() {
        return new DoNothingEventeumEventBroadcaster();
    }
}
