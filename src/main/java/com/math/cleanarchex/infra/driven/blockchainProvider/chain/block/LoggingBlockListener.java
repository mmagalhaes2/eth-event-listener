package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class LoggingBlockListener implements BlockListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggingBlockListener.class);

    @Override
    public void onBlock(Block block) {
        logger.info("New block mined. Hash: {}, Number: {}", block.getHash(), block.getNumber());
    }
}
