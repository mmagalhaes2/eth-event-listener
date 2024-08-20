package com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Transaction;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;

public interface TransactionDetailsFactory {
    TransactionDetails createTransactionDetails(
            Transaction transaction, TransactionStatus status, Block block);
}
