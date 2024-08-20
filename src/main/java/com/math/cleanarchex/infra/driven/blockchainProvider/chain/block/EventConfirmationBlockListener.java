package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Log;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.TransactionReceipt;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.Node;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventStatus;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class EventConfirmationBlockListener extends AbstractConfirmationBlockListener<ContractEventDetails> {

    private static final Logger LOG = LoggerFactory.getLogger(EventConfirmationBlockListener.class);

    private ContractEventDetails contractEvent;
    private BlockchainEventBroadcaster eventBroadcaster;


    public EventConfirmationBlockListener(ContractEventDetails contractEvent,
                                          BlockchainService blockchainService,
                                          BlockSubscriptionStrategy blockSubscription,
                                          BlockchainEventBroadcaster eventBroadcaster,
                                          Node node) {
        super(contractEvent, blockchainService, blockSubscription, node);
        this.contractEvent = contractEvent;
        this.eventBroadcaster = eventBroadcaster;
    }


    @Override
    protected boolean isOrphaned(TransactionReceipt receipt) {
        final Optional<Log> log = getCorrespondingLog(receipt);


        if (log.isPresent()) {

            if (log.get().isRemoved()) {
                LOG.info("Orphan event detected: isRemoved == true");

                return true;
            }

            return super.isOrphaned(receipt);

        } else {

            return true;
        }
    }

    @Override
    protected String getEventIdentifier(ContractEventDetails contractEventDetails) {
        return contractEventDetails.getId();
    }

    @Override
    protected void setStatus(ContractEventDetails contractEventDetails, String status) {
        contractEventDetails.setStatus(ContractEventStatus.valueOf(status));
    }

    @Override
    protected void broadcast(ContractEventDetails contractEventDetails) {
        eventBroadcaster.broadcastContractEvent(contractEvent);
    }

    private Optional<Log> getCorrespondingLog(TransactionReceipt receipt) {
        return receipt.getLogs()
                .stream()
                .filter((log) -> log.getLogIndex().equals(contractEvent.getLogIndex()))
                .findFirst();
    }
}
