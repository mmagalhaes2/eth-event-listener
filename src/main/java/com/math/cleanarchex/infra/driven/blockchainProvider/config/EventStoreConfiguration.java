package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.BlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.EventStoreLatestBlockUpdater;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.ContractEventListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.EventStoreContractEventUpdater;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.BlockDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.factory.EventStoreFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.EventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.SaveableEventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db.MongoEventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db.repository.ContractEventDetailsRepository;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db.repository.LatestBlockRepository;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.rest.RESTEventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.rest.client.EventStoreClient;
import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.EventeumValueMonitor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Order(0)
public class EventStoreConfiguration {

    @Configuration
    @ConditionalOnExpression("'${eventStore.type}:${database.type}'=='DB:MONGO'")
    @ConditionalOnMissingBean(EventStoreFactory.class)
    public static class MongoEventStoreConfiguration {

        @Bean
        public SaveableEventStore dbEventStore(
                ContractEventDetailsRepository contractEventRepository,
                LatestBlockRepository latestBlockRepository,
                MongoTemplate mongoTemplate) {
            return new MongoEventStore(contractEventRepository, latestBlockRepository, mongoTemplate);
        }

        @Bean
        public ContractEventListener eventStoreContractEventUpdater(SaveableEventStore eventStore) {
            return new EventStoreContractEventUpdater(eventStore);
        }

        @Bean
        public BlockListener eventStoreLatestBlockUpdater(SaveableEventStore eventStore,
                                                          BlockDetailsFactory blockDetailsFactory,
                                                          EventeumValueMonitor valueMonitor,
                                                          ChainServicesContainer chainServicesContainer) {
            return new EventStoreLatestBlockUpdater(eventStore, blockDetailsFactory, valueMonitor, chainServicesContainer);
        }
    }

    @Configuration
    @ConditionalOnExpression("'${eventStore.type}:${database.type}'=='DB:SQL'")
    @ConditionalOnMissingBean(EventStoreFactory.class)
    public static class SqlEventStoreConfiguration {


        @Bean
        public ContractEventListener eventStoreContractEventUpdater(SaveableEventStore eventStore) {
            return new EventStoreContractEventUpdater(eventStore);
        }

        @Bean
        public BlockListener eventStoreLatestBlockUpdater(SaveableEventStore eventStore,
                                                          BlockDetailsFactory blockDetailsFactory,
                                                          EventeumValueMonitor valueMonitor,
                                                          ChainServicesContainer chainServiceContainer) {
            return new EventStoreLatestBlockUpdater(eventStore, blockDetailsFactory, valueMonitor, chainServiceContainer);
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "eventStore.type", havingValue = "REST")
    @ConditionalOnMissingBean(EventStoreFactory.class)
    public static class RESTEventStoreConfiguration {

        @Bean
        public EventStore RESTEventStore(EventStoreClient client) {
            return new RESTEventStore(client);
        }
    }


}
