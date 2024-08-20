package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;
import com.math.cleanarchex.infra.handler.exception.NotFoundException;

public interface TransactionMonitoringService {

    void registerTransactionsToMonitor(TransactionMonitoringSpec spec);

    void registerTransactionsToMonitor(TransactionMonitoringSpec spec, boolean broadcast);

    void stopMonitoringTransactions(String id) throws NotFoundException;

    void stopMonitoringTransactions(String id, boolean broadcast) throws NotFoundException;
}
