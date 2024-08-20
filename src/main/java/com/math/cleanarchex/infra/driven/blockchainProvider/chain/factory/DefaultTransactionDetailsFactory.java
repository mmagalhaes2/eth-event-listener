package com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Transaction;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Keys;

@Component
public class DefaultTransactionDetailsFactory implements TransactionDetailsFactory {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TransactionDetails createTransactionDetails(
            Transaction transaction, TransactionStatus status, Block block) {

        final TransactionDetails transactionDetails = new TransactionDetails();
        modelMapper.map(transaction, transactionDetails);

        transactionDetails.setNodeName(block.getNodeName());
        transactionDetails.setTimestamp(block.getTimestamp());
        transactionDetails.setStatus(status);

        if (transaction.getCreates() != null) {
            transactionDetails.setContractAddress(Keys.toChecksumAddress(transaction.getCreates()));
        }

        return transactionDetails;
    }
}
