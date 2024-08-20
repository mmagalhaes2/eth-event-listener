

package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;

import java.util.List;

public class ToAddressMatchingCriteria extends SingleValueMatchingCriteria<String> {

    public ToAddressMatchingCriteria(String nodeName, String toAddress, List<TransactionStatus> statuses) {
        super(nodeName, toAddress, statuses);
    }

    @Override
    protected String getValueFromTx(TransactionDetails tx) {
        return tx.getTo();
    }

    @Override
    public boolean isOneTimeMatch() {
        return false;
    }
}
