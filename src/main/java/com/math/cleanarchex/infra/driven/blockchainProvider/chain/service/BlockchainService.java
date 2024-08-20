package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.ContractEventListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.TransactionReceipt;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.FilterSubscription;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface BlockchainService {

    /**
     * @return The ethereum node name that this service is connected to.
     */
    String getNodeName();

    /**
     * Retrieves all events for a specified event filter.
     *
     * @param eventFilter The contract event filter that should be matched.
     * @param startBlock  The start block
     * @param endBlock    The end block
     * @return The blockchain contract events
     */
    List<ContractEventDetails> retrieveEvents(ContractEventFilter eventFilter,
                                              BigInteger startBlock,
                                              BigInteger endBlock);

    /**
     * Register a contract event listener for the specified event filter, that gets triggered when an event
     * matching the filter is emitted within the Ethereum network.
     *
     * @param eventFilter   The contract event filter that should be matched.
     * @param eventListener The listener to be triggered when a matching event is emitted
     * @return The registered subscription
     */
    FilterSubscription registerEventListener(ContractEventFilter eventFilter,
                                             ContractEventListener eventListener,
                                             BigInteger startBlock,
                                             BigInteger endBlock,
                                             Optional<Runnable> onCompletion);

    /**
     * @return the client version for the connected Ethereum node.
     */
    String getClientVersion();

    /**
     * @return the current block number of the network that the Ethereum node is connected to.
     */
    BigInteger getCurrentBlockNumber();

    /**
     * @param blockHash              The hash of the block to obtain
     * @param fullTransactionObjects If full transaction details should be populated
     * @return The block for the specified hash or nothing if a block with the specified hash does not exist.
     */
    public Optional<Block> getBlock(String blockHash, boolean fullTransactionObjects);

    List<ContractEventDetails> getEventsForFilter(ContractEventFilter filter, BigInteger blockNumber);

    /**
     * Obtain the transaction receipt for a specified transaction id.
     *
     * @param txId the transaction id
     * @return the receipt for the transaction with the specified id.
     */
    TransactionReceipt getTransactionReceipt(String txId);


    String getRevertReason(String from, String to, BigInteger blockNumber, String input);
}
