package com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;

public interface ContractEventListener {

    /**
     * Called when an event is fired for any configured contract events within the system.
     *
     * @param eventDetails The details of the new event.
     */
    void onEvent(ContractEventDetails eventDetails);
}
