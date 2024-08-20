package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.BlockDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.SaveableEventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.EventeumValueMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Order(Ordered.LOWEST_PRECEDENCE)
public class EventStoreLatestBlockUpdater implements BlockListener {

    private final SaveableEventStore saveableEventStore;

    private final BlockDetailsFactory blockDetailsFactory;
    private final Map<String, AtomicLong> latestBlockMap;

    @Autowired
    public EventStoreLatestBlockUpdater(SaveableEventStore saveableEventStore,
                                        BlockDetailsFactory blockDetailsFactory,
                                        EventeumValueMonitor valueMonitor,
                                        ChainServicesContainer chainServicesContainer) {
        this.saveableEventStore = saveableEventStore;
        this.latestBlockMap = new HashMap<>();
        this.blockDetailsFactory = blockDetailsFactory;

        chainServicesContainer.getNodeNames().forEach(node -> {
            this.latestBlockMap.put(node, valueMonitor.monitor("latestBlock", node, new AtomicLong(0)));
        });
    }

    @Override
    public void onBlock(Block block) {
        saveableEventStore.save(new LatestBlock(blockDetailsFactory.createBlockDetails(block)));
        latestBlockMap.get(block.getNodeName()).set(block.getNumber().longValue());

    }
}
