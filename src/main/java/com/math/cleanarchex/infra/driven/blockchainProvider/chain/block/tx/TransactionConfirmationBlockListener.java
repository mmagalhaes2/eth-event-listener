package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.AbstractConfirmationBlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.Node;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.BlockchainEventBroadcaster;

import java.util.List;

public class TransactionConfirmationBlockListener extends AbstractConfirmationBlockListener<TransactionDetails> {

    private BlockchainEventBroadcaster eventBroadcaster;
    private OnConfirmedCallback onConfirmedCallback;
    private List<TransactionStatus> statusesToFilter;

    public TransactionConfirmationBlockListener(TransactionDetails transactionDetails,
                                                BlockchainService blockchainService,
                                                BlockSubscriptionStrategy blockSubscription,
                                                BlockchainEventBroadcaster eventBroadcaster,
                                                Node node,
                                                List<TransactionStatus> statusesToFilter,
                                                OnConfirmedCallback onConfirmedCallback) {
        super(transactionDetails, blockchainService, blockSubscription, node);
        this.eventBroadcaster = eventBroadcaster;
        this.onConfirmedCallback = onConfirmedCallback;
        this.statusesToFilter = statusesToFilter;
    }

    @Override
    protected void broadcastEventConfirmed() {
        super.broadcastEventConfirmed();

        onConfirmedCallback.onConfirmed();
    }

    @Override
    protected String getEventIdentifier(TransactionDetails transactionDetails) {
        return transactionDetails.getHash() + transactionDetails.getBlockHash();
    }

    @Override
    protected void setStatus(TransactionDetails transactionDetails, String status) {
        transactionDetails.setStatus(TransactionStatus.valueOf(status));
    }

    @Override
    protected void broadcast(TransactionDetails transactionDetails) {
        if (statusesToFilter.contains(transactionDetails.getStatus())) {
            eventBroadcaster.broadcastTransaction(transactionDetails);
        }
    }

    public interface OnConfirmedCallback {
        void onConfirmed();
    }
}
