package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.FromAddressMatchingCriteria;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.ToAddressMatchingCriteria;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.TransactionMatchingCriteria;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.TxHashMatchingCriteria;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionIdentifierType;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;
import org.springframework.stereotype.Component;

@Component
public class DefaultTransactionMatchingCriteriaFactory implements TransactionMatchingCriteriaFactory {

    @Override
    public TransactionMatchingCriteria build(TransactionMonitoringSpec spec) {
        if (spec.getType() == TransactionIdentifierType.HASH) {
            return new TxHashMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        if (spec.getType() == TransactionIdentifierType.TO_ADDRESS) {
            return new ToAddressMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        if (spec.getType() == TransactionIdentifierType.FROM_ADDRESS) {
            return new FromAddressMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        throw new UnsupportedOperationException("Type: " + spec.getType() + " not currently supported");
    }
}
