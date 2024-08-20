package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.BlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.TransactionMatchingCriteria;

public interface TransactionMonitoringBlockListener extends BlockListener {

    void addMatchingCriteria(TransactionMatchingCriteria matchingCriteria);

    void removeMatchingCriteria(TransactionMatchingCriteria matchingCriteria);
}
