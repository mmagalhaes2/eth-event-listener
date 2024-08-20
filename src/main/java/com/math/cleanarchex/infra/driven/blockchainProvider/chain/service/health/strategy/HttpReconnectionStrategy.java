package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health.strategy;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpReconnectionStrategy extends ResubscribingReconnectionStrategy {

    public HttpReconnectionStrategy(SubscriptionService subscriptionService, BlockSubscriptionStrategy blockSubscription) {
        super(subscriptionService, blockSubscription);
    }

    @Override
    public void reconnect() {
        //Do Nothing
    }
}
