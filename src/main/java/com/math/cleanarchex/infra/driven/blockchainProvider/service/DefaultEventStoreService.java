package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.EventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultEventStoreService implements EventStoreService {

    private final EventStore eventStore;

    /**
     * @{inheritDoc}
     */
    @Override
    public Optional<ContractEventDetails> getLatestContractEvent(
            String eventSignature, String contractAddress) {
        int page = eventStore.isPagingZeroIndexed() ? 0 : 1;

        final PageRequest pagination = PageRequest.of(page,
                1, Sort.by(Sort.Direction.DESC, "blockNumber"));

        final Page<ContractEventDetails> eventsPage =
                eventStore.getContractEventsForSignature(eventSignature, contractAddress, pagination);

        if (eventsPage == null) {
            return Optional.empty();
        }

        final List<ContractEventDetails> events = eventsPage.getContent();

        if (events == null || events.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(events.getFirst());
    }

    @Override
    public Optional<LatestBlock> getLatestBlock(String nodeName) {

        return eventStore.getLatestBlockForNode(nodeName);
    }
}
