package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block;

import com.google.common.collect.EvictingQueue;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DefaultBlockCache implements BlockCache {

    private static final Integer CACHE_SIZE = 3;

    private final EvictingQueue<Block> cachedBlocks = EvictingQueue.create(CACHE_SIZE);

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void add(Block block) {
        lock.lock();

        try {
            cachedBlocks.add(block);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<Block> getCachedBlocks() {
        lock.lock();

        try {
            return new HashSet<>(cachedBlocks);
        } finally {
            lock.unlock();
        }
    }
}
