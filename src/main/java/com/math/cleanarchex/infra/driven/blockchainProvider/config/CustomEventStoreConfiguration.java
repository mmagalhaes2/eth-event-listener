package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.BlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.EventStoreLatestBlockUpdater;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.ContractEventListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.EventStoreContractEventUpdater;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.BlockDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.factory.EventStoreFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.SaveableEventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.EventeumValueMonitor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@ConditionalOnBean(EventStoreFactory.class)
public class CustomEventStoreConfiguration {

    @Bean
    public SaveableEventStore customEventStore(EventStoreFactory factory) {
        return factory.build();
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
