package com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.block.BlockDetails;

public interface BlockDetailsFactory {

    BlockDetails createBlockDetails(Block block);
}
