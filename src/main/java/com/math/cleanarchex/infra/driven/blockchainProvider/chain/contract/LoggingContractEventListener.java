package com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class LoggingContractEventListener implements ContractEventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggingContractEventListener.class);

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        logger.info("Contract event fired: {}", eventDetails.getName());
    }
}
