package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.BlockDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BroadcastingBlockListener implements BlockListener {

    private BlockchainEventBroadcaster eventBroadcaster;

    private BlockDetailsFactory blockDetailsFactory;

    @Override
    public void onBlock(Block block) {
        eventBroadcaster.broadcastNewBlock(blockDetailsFactory.createBlockDetails(block));
    }


}
