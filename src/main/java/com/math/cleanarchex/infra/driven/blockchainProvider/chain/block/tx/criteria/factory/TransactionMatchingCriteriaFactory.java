package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.TransactionMatchingCriteria;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;

public interface TransactionMatchingCriteriaFactory {

    TransactionMatchingCriteria build(TransactionMonitoringSpec spec);
}
