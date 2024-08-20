package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain;

import java.math.BigInteger;
import java.util.List;

public interface TransactionReceipt {

    String getTransactionHash();

    BigInteger getTransactionIndex();

    String getBlockHash();

    BigInteger getBlockNumber();

    BigInteger getCumulativeGasUsed();

    BigInteger getGasUsed();

    String getContractAddress();

    String getRoot();

    String getFrom();

    String getTo();

    List<Log> getLogs();

    String getLogsBloom();

    String getStatus();
}
