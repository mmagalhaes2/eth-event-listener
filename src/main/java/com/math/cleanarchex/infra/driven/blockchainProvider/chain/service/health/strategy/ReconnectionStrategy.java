package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health.strategy;

public interface ReconnectionStrategy {

    /**
     * Triggered when an Ethereum node failure is detected.
     */
    void reconnect();

    /**
     * Triggered when it has been detected that the Ethereum node has recovered after failure.
     */
    void resubscribe();
}
