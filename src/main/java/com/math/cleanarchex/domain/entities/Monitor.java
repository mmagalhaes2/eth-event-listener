package com.math.cleanarchex.domain.entities;

public record Monitor(String monitorId,
                      String walletAddress,
                      String nodeIdentifier,
                      EventSpecification eventSpecification,
                      TransactionSpecification transactionSpecification,
                      CorrelationStrategy correlationStrategy,
                      String callbackUrl,
                      Boolean isActive) {
}
