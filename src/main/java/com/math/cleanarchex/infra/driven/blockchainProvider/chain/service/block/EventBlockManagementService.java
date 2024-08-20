package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;

import java.math.BigInteger;

public interface EventBlockManagementService {

    /**
     * Update the latest block number state for an event specification.
     *
     * @param eventSpecHash The event specification hash.
     * @param blockNumber   The new latest block number.
     * @param address       The address of the contract.
     */
    void updateLatestBlock(String eventSpecHash, BigInteger blockNumber, String address);

    /**
     * Retrieve the latest block number that has been seen for a specified event specification.
     *
     * @param eventFilter The event filter.
     * @return The latest block number that has been seen for a specified event specification.
     */
    BigInteger getLatestBlockForEvent(ContractEventFilter eventFilter);
}
