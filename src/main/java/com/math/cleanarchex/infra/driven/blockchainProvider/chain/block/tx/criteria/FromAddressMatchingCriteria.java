package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;

import java.util.List;

public class FromAddressMatchingCriteria extends SingleValueMatchingCriteria<String> {

    public FromAddressMatchingCriteria(String nodeName, String fromAddress, List<TransactionStatus> statuses) {
        super(nodeName, fromAddress, statuses);
    }

    @Override
    protected String getValueFromTx(TransactionDetails tx) {
        return tx.getFrom();
    }

    @Override
    public boolean isOneTimeMatch() {
        return false;
    }
}
