package com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.block.BlockDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * {@inheritDoc}
 * <p>
 * This broadcaster also ensures that the same message is only sent once
 * (by storing sent events in a short lives cache and not sending events if a cache match is found).
 * <p>
 * The cache expiration time can be configured with the broadcaster.cache.expirationMillis property.
 */
public class EventBroadcasterWrapper implements BlockchainEventBroadcaster {

    private Cache<Integer, ContractEventDetails> contractEventCache;

    private Cache<Integer, TransactionDetails> transactionCache;

    private Cache<Integer, TransactionDetails> transactionDetailsCache;

    private Long expirationTimeMillis;

    private BlockchainEventBroadcaster wrapped;

    private boolean enableBlockNotifications;

    public EventBroadcasterWrapper(Long expirationTimeMillis,
                                   BlockchainEventBroadcaster toWrap,
                                   boolean enableBlockNotifications) {
        this.expirationTimeMillis = expirationTimeMillis;
        this.contractEventCache = createCache(ContractEventDetails.class);
        this.transactionCache = createCache(TransactionDetails.class);
        this.wrapped = toWrap;
        this.enableBlockNotifications = enableBlockNotifications;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        if (!this.enableBlockNotifications) {
            return;
        }

        wrapped.broadcastNewBlock(block);
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        synchronized (this) {
            if (contractEventCache.getIfPresent(Integer.valueOf(eventDetails.hashCode())) == null) {
                contractEventCache.put(Integer.valueOf(eventDetails.hashCode()), eventDetails);
                wrapped.broadcastContractEvent(eventDetails);
            }
        }
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        synchronized (this) {
            if (transactionCache.getIfPresent(Integer.valueOf(transactionDetails.hashCode())) == null) {
                transactionCache.put(Integer.valueOf(transactionDetails.hashCode()), transactionDetails);
                wrapped.broadcastTransaction(transactionDetails);
            }
        }
    }

    @Scheduled(fixedRateString = "${broadcaster.cache.expirationMillis}")
    public void cleanUpCache() {
        contractEventCache.cleanUp();
        transactionCache.cleanUp();
    }

    protected <T> Cache<Integer, T> createCache(Class<T> clazz) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTimeMillis, TimeUnit.MILLISECONDS)
                .build();
    }
}
