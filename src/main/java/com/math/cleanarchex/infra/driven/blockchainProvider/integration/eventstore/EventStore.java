package com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface for integrating with an event store, in order to obtain events for a specified signature.
 */
public interface EventStore {
    Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination);

    Optional<LatestBlock> getLatestBlockForNode(String nodeName);

    boolean isPagingZeroIndexed();
}
