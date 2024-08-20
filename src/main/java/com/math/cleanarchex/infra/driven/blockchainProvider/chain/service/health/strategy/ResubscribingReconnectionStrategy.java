package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health.strategy;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class ResubscribingReconnectionStrategy implements ReconnectionStrategy {

    private SubscriptionService subscriptionService;
    private BlockSubscriptionStrategy blockSubscriptionStrategy;

    @Override
    public void resubscribe() {

        blockSubscriptionStrategy.subscribe();
    }
}
