package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain;

import java.math.BigInteger;
import java.util.List;


public interface Log {

    boolean isRemoved();

    BigInteger getLogIndex();

    BigInteger getTransactionIndex();

    String getTransactionHash();

    String getBlockHash();

    BigInteger getBlockNumber();

    String getAddress();

    String getData();

    String getType();

    List<String> getTopics();
}
