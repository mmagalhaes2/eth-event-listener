package com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;

import java.util.List;

public interface TransactionMatchingCriteria {

    String getNodeName();

    List<TransactionStatus> getStatuses();

    boolean isAMatch(TransactionDetails tx);

    boolean isOneTimeMatch();
}
