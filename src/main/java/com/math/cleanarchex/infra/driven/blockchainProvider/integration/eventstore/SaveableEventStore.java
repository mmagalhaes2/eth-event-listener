package com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;

/**
 * Interface for integrating with an event store that supports direct saving of events.
 */
public interface SaveableEventStore extends EventStore {
    void save(ContractEventDetails contractEventDetails);

    void save(LatestBlock latestBlock);
}
