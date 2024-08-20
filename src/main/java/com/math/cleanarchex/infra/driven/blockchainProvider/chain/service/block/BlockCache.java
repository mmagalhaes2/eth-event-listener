package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;

import java.util.Set;

public interface BlockCache {

    void add(Block block);

    Set<Block> getCachedBlocks();
}
