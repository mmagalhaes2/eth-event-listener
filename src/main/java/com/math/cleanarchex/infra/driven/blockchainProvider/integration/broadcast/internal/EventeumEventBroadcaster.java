package com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.internal;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;

/**
 * An interface for a class that broadcasts Eventeum internal events to other Eventeum instances in the wider system.
 */
public interface EventeumEventBroadcaster {

    /**
     * Broadcasts the details of a contract event filter that has been added to this Eventeum instance.
     *
     * @param filter the filter in question.
     */
    void broadcastEventFilterAdded(ContractEventFilter filter);

    /**
     * Broadcasts the details of a contract event filter that has been removed from this Eventeum instance.
     *
     * @param filter the filter in question.
     */
    void broadcastEventFilterRemoved(ContractEventFilter filter);

    /**
     * Broadcasts the details of a transaction monitoring spec that has been added to this Eventeum instance.
     *
     * @param spec the transaction monitoring spec in question.
     */
    void broadcastTransactionMonitorAdded(TransactionMonitoringSpec spec);

    /**
     * Broadcasts the details of a transaction monitoring spec that has been removed from this Eventeum instance.
     *
     * @param spec the transaction monitoring spec in question.
     */
    void broadcastTransactionMonitorRemoved(TransactionMonitoringSpec spec);
}
