package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.ContractEventProcessor;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class EventProcessingBlockListener implements BlockListener {

    @Lazy
    private SubscriptionService subscriptionService;

    private final ContractEventProcessor contractEventProcessor;

    @Autowired
    public EventProcessingBlockListener(@Lazy SubscriptionService subscriptionService,
                                        ContractEventProcessor contractEventProcessor) {
        this.subscriptionService = subscriptionService;
        this.contractEventProcessor = contractEventProcessor;
    }

    @Override
    public void onBlock(Block block) {
        contractEventProcessor.processLogsInBlock(block, subscriptionService.listContractEventFilters());
    }
}
