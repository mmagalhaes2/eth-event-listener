package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;

import java.util.Optional;

public interface EventStoreService {

    /**
     * Returns the contract event with the latest block, that matches the event signature.
     *
     * @param eventSignature  The event signature
     * @param contractAddress The event contract address
     * @return The event details
     */
    Optional<ContractEventDetails> getLatestContractEvent(String eventSignature, String contractAddress);

    /**
     * Returns the latest block, for the specified node.
     *
     * @param nodeName The nodename
     * @return The block details
     */
    Optional<LatestBlock> getLatestBlock(String nodeName);
}
