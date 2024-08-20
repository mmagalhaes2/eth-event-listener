package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;

public interface BlockListener {

    /**
     * Called when a new block is detected fron the ethereum node.
     *
     * @param block The new block
     */
    void onBlock(Block block);
}
