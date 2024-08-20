package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block;

import java.math.BigInteger;

public interface BlockNumberService {

    BigInteger getStartBlockForNode(String nodeName);
}
