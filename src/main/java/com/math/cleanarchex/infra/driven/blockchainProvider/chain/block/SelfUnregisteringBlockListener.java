package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;

public abstract class SelfUnregisteringBlockListener implements BlockListener {

    private final BlockSubscriptionStrategy blockSubscriptionStrategy;

    protected SelfUnregisteringBlockListener(BlockSubscriptionStrategy blockSubscriptionStrategy) {
        this.blockSubscriptionStrategy = blockSubscriptionStrategy;
    }

    protected void unregister() {
        blockSubscriptionStrategy.removeBlockListener(this);
    }
}
