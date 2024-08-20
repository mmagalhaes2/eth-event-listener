

package com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.Node;

public interface BlockchainServiceFactory {

    BlockchainService create(Node node);
}
