package com.math.cleanarchex.domain.entities;

import java.util.List;

public record EventDetails(String name,
                           String filterId,
                           List<Parameter> parameters,
                           String transactionHash,
                           Integer logIndex,
                           Integer blockNumber,
                           String blockHash,
                           String address,
                           String status,
                           String eventSpecificationSignature,
                           String id,
                           Integer number,
                           String hash,
                           Integer timestamp,
                           String nonce,
                           String transactionIndex,
                           String from,
                           String to,
                           String value,
                           String nodeName) {
}
