package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import lombok.Data;
import org.web3j.protocol.Web3j;

@Data
public class NodeServices {

    private String nodeName;

    private Web3j web3j;

    private BlockchainService blockchainService;

    private BlockSubscriptionStrategy blockSubscriptionStrategy;
}
