package com.math.cleanarchex.infra.driven.blockchainProvider.service.sync;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.ContractEventProcessor;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block.BlockNumberService;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.EventFilterSyncStatus;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.SyncStatus;
import com.math.cleanarchex.infra.persistence.EventFilterSyncStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DefaultEventSyncService implements EventSyncService {

    private final BlockNumberService blockNumberService;

    private final EventRetriever eventRetriever;

    private final EventFilterSyncStatusRepository syncStatusRepository;

    private final ContractEventProcessor contractEventProcessor;

    private final @Qualifier("eternalRetryTemplate") RetryTemplate retryTemplate;

    public DefaultEventSyncService(BlockNumberService blockNumberService,
                                   EventRetriever eventRetriever,
                                   EventFilterSyncStatusRepository syncStatusRepository,
                                   ContractEventProcessor contractEventProcessor,
                                   @Qualifier("eternalRetryTemplate") RetryTemplate retryTemplate) {
        this.blockNumberService = blockNumberService;
        this.eventRetriever = eventRetriever;
        this.syncStatusRepository = syncStatusRepository;
        this.contractEventProcessor = contractEventProcessor;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void sync(List<ContractEventFilter> filters) {

        filters.forEach(filter -> retryTemplate.execute((context) -> {
            syncFilter(filter);
            return null;
        }));
    }

    private void syncFilter(ContractEventFilter filter) {
        final Optional<EventFilterSyncStatus> syncStatus = syncStatusRepository.findById(filter.getId());

        if (!syncStatus.isPresent() || syncStatus.get().getSyncStatus() == SyncStatus.NOT_SYNCED) {
            final BigInteger startBlock = getStartBlock(filter, syncStatus);
            //Should sync to block start block number
            final BigInteger endBlock = blockNumberService.getStartBlockForNode(filter.getNode());

            log.info("Syncing event filter with id {} from block {} to {}", filter.getId(), startBlock, endBlock);

            eventRetriever.retrieveEvents(filter, startBlock, endBlock,
                    (events) -> events.forEach(this::processEvent));

            final EventFilterSyncStatus finalSyncStatus = getEventSyncStatus(filter.getId());
            finalSyncStatus.setSyncStatus(SyncStatus.SYNCED);
            syncStatusRepository.save(finalSyncStatus);

            log.info("Event filter with id {} has completed syncing", filter.getId());

        } else {
            log.info("Event filter with id {} already synced", filter.getId());
        }
    }

    private void processEvent(ContractEventDetails contractEvent) {
        contractEventProcessor.processContractEvent(contractEvent);

        final EventFilterSyncStatus syncStatus = getEventSyncStatus(contractEvent.getFilterId());

        syncStatus.setLastBlockNumber(contractEvent.getBlockNumber());
        syncStatusRepository.save(syncStatus);
    }

    private EventFilterSyncStatus getEventSyncStatus(String id) {
        return syncStatusRepository.findById(id)
                .orElse(EventFilterSyncStatus
                        .builder()
                        .filterId(id)
                        .syncStatus(SyncStatus.NOT_SYNCED)
                        .build());
    }

    private BigInteger getStartBlock(ContractEventFilter contractEventFilter,
                                     Optional<EventFilterSyncStatus> syncStatus) {

        if (syncStatus.isPresent() && syncStatus.get().getLastBlockNumber().compareTo(BigInteger.ZERO) > 0) {
            return syncStatus.get().getLastBlockNumber();
        }

        return contractEventFilter.getStartBlock();
    }
}
