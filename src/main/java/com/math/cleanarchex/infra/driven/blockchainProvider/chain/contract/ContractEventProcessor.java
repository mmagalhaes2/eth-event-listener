package com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;

import java.util.List;

public interface ContractEventProcessor {

    void processLogsInBlock(Block block, List<ContractEventFilter> contractEventFilters);

    void processContractEvent(ContractEventDetails contractEventDetails);
}
