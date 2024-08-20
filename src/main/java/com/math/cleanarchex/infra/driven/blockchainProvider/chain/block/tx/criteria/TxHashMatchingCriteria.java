package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;

import java.util.List;

public class TxHashMatchingCriteria extends SingleValueMatchingCriteria<String> {

    public TxHashMatchingCriteria(String nodeName, String hashToMatch, List<TransactionStatus> statuses) {
        super(nodeName, hashToMatch, statuses);
    }

    @Override
    protected String getValueFromTx(TransactionDetails tx) {
        return tx.getHash();
    }

    @Override
    public boolean isOneTimeMatch() {
        return true;
    }
}
