package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.TransactionMonitoringBlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.TransactionMatchingCriteria;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.tx.criteria.factory.TransactionMatchingCriteriaFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.TransactionDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block.BlockCache;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.internal.EventeumEventBroadcaster;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;
import com.math.cleanarchex.infra.handler.exception.NotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultTransactionMonitoringService implements TransactionMonitoringService {

    private final ChainServicesContainer chainServices;

    private final BlockchainEventBroadcaster broadcaster;

    private final EventeumEventBroadcaster eventeumEventBroadcaster;

    private final TransactionDetailsFactory transactionDetailsFactory;

    private final com.math.cleanarchex.infra.persistence.TransactionMonitoringSpecRepository transactionMonitoringRepo;

    private final TransactionMonitoringBlockListener monitoringBlockListener;

    private final TransactionMatchingCriteriaFactory matchingCriteriaFactory;

    private final BlockCache blockCache;

    private final Map<String, TransactionMonitor> transactionMonitors = new HashMap<>();

    @Override
    public void registerTransactionsToMonitor(TransactionMonitoringSpec spec) {
        registerTransactionsToMonitor(spec, true);
    }

    @Override
    public void registerTransactionsToMonitor(TransactionMonitoringSpec spec, boolean broadcast) {
        if (isTransactionSpecRegistered(spec)) {
            log.info("Already registered transaction monitoring spec with id: " + spec.getId());
            return;
        }

        registerTransactionMonitoring(spec);
        saveTransactionMonitoringSpec(spec);

        if (broadcast) {
            eventeumEventBroadcaster.broadcastTransactionMonitorAdded(spec);
        }
    }

    @Override
    public void stopMonitoringTransactions(String monitorId) throws NotFoundException {
        stopMonitoringTransactions(monitorId, true);
    }

    @Override
    public void stopMonitoringTransactions(String monitorId, boolean broadcast) throws NotFoundException {

        final TransactionMonitor transactionMonitor = getTransactionMonitor(monitorId);

        if (transactionMonitor == null) {
            throw new NotFoundException("No monitored transaction with id: " + monitorId);
        }

        removeTransactionMonitorMatchinCriteria(transactionMonitor);
        deleteTransactionMonitor(monitorId);

        if (broadcast) {
            eventeumEventBroadcaster.broadcastTransactionMonitorRemoved(transactionMonitor.getSpec());
        }
    }

    private void removeTransactionMonitorMatchinCriteria(TransactionMonitor transactionMonitor) {
        monitoringBlockListener.removeMatchingCriteria(transactionMonitor.getMatchingCriteria());
    }

    private void deleteTransactionMonitor(String monitorId) {
        transactionMonitors.remove(monitorId);

        transactionMonitoringRepo.deleteById(monitorId);
    }

    private TransactionMonitor getTransactionMonitor(String monitorId) {
        return transactionMonitors.get(monitorId);
    }

    private void registerTransactionMonitoring(TransactionMonitoringSpec spec) {

        final TransactionMatchingCriteria matchingCriteria = matchingCriteriaFactory.build(spec);
        monitoringBlockListener.addMatchingCriteria(matchingCriteria);

        transactionMonitors.put(spec.getId(), new TransactionMonitor(spec, matchingCriteria));
    }

    private TransactionMonitoringSpec saveTransactionMonitoringSpec(TransactionMonitoringSpec spec) {
        return transactionMonitoringRepo.save(spec);
    }

    private boolean isTransactionSpecRegistered(TransactionMonitoringSpec spec) {
        return transactionMonitors.containsKey(spec.getId());
    }

    @Data
    private class TransactionMonitor {
        TransactionMonitoringSpec spec;

        TransactionMatchingCriteria matchingCriteria;

        public TransactionMonitor(TransactionMonitoringSpec spec, TransactionMatchingCriteria matchingCriteria) {
            this.spec = spec;
            this.matchingCriteria = matchingCriteria;
        }

    }
}
