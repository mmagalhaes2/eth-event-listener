package com.math.cleanarchex.infra.driven.blockchainProvider.dto.message;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.block.BlockDetails;


public class BlockEvent extends AbstractMessage<BlockDetails> {
    public static final String TYPE = "BLOCK";

    public BlockEvent() {
    }

    public BlockEvent(BlockDetails details) {
        super(details.getHash(), TYPE, details);
    }
}
