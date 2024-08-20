package com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import org.web3j.protocol.core.methods.response.Log;

public interface ContractEventDetailsFactory {
    ContractEventDetails createEventDetails(ContractEventFilter eventFilter, Log log);
}
