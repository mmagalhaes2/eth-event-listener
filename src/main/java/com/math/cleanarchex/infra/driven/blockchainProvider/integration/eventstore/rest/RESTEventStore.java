package com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.rest;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.EventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.rest.client.EventStoreClient;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * An event store implementation that integrates with an external REST api in order to obtain the event details.
 * <p>
 * The REST events tore path can be specified with the eventStore.url and eventStore.eventPath parameters.
 */
public class RESTEventStore implements EventStore {

    private final EventStoreClient client;

    public RESTEventStore(EventStoreClient client) {
        this.client = client;
    }

    @Override
    public Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination) {

        final Sort.Order firstOrder = pagination.getSort().iterator().next();
        return client.getContractEvents(pagination.getPageNumber(),
                pagination.getPageSize(),
                firstOrder.getProperty(),
                firstOrder.getDirection(),
                eventSignature,
                contractAddress);
    }

    @Override
    public Optional<LatestBlock> getLatestBlockForNode(String nodeName) {
        return Optional.ofNullable(client.getLatestBlock(nodeName));
    }

    @Override
    public boolean isPagingZeroIndexed() {
        return true;
    }
}
