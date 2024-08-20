package com.math.cleanarchex.infra.driven.blockchainProvider.service.sync;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Consumer;

public interface EventRetriever {

    void retrieveEvents(ContractEventFilter eventFilter,
                        BigInteger startBlock,
                        BigInteger endBlock,
                        Consumer<List<ContractEventDetails>> eventConsumer);
}
