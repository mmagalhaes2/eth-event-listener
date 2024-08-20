package com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.block.BlockDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;

public interface BlockchainEventBroadcaster {

    /**
     * Broadcast details of a new block that has been mined.
     *
     * @param block
     */
    void broadcastNewBlock(BlockDetails block);

    /**
     * Broadcasts details of a new smart contract event that has been emitted from the ethereum blockchain.
     *
     * @param eventDetails
     */
    void broadcastContractEvent(ContractEventDetails eventDetails);

    /**
     * Broadcasts details of a monitored transaction that has been mined.
     *
     * @param transactionDetails
     */
    void broadcastTransaction(TransactionDetails transactionDetails);
}
