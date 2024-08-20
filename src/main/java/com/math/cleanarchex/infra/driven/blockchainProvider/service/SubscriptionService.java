package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.handler.exception.NotFoundException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubscriptionService {

    /**
     * Initialise the subscription service
     */
    void init(List<ContractEventFilter> initFilters);

    /**
     * Registers a new contract event filter.
     * <p>
     * If the id is null, then one is assigned.
     *
     * @param filter    The filter to add.
     * @param broadcast Specifies if the added filter event should be broadcast to other Eventeum instances.
     * @return The registered contract event filter
     */
    ContractEventFilter registerContractEventFilter(ContractEventFilter filter, boolean broadcast);

    /**
     * Registers a new contract event filter.
     * <p>
     * If the id is null, then one is assigned.
     * <p>
     * Will retry indefinitely until successful
     *
     * @param filter    The filter to add.
     * @param broadcast Specifies if the added filter event should be broadcast to other Eventeum instances.
     * @return The registered contract event filter
     */
    CompletableFuture<ContractEventFilter> registerContractEventFilterWithRetries(ContractEventFilter filter, boolean broadcast);

    /**
     * List all registered contract event filters.
     *
     * @return The list of registered contract event filters
     */
    List<ContractEventFilter> listContractEventFilters();

    /**
     * Unregisters a previously added contract event filter.
     * <p>
     * Broadcasts the removed filter event to any other Eventeum instances.
     *
     * @param filterId The filter id of the event to remove.
     */
    void unregisterContractEventFilter(String filterId) throws NotFoundException;

    /**
     * Unregisters a previously added contract event filter.
     *
     * @param filterId  The filter id of the event to remove.
     * @param broadcast Specifies if the removed filter event should be broadcast to other Eventeum instances.
     */
    void unregisterContractEventFilter(String filterId, boolean broadcast) throws NotFoundException;

    /**
     * Unsubscribe all active listeners
     */
    void unsubscribeToAllSubscriptions(String nodeName);

    /**
     * @return the state of the service
     */
    SubscriptionServiceState getState();

    enum SubscriptionServiceState {
        UNINITIALISED,

        SYNCING_EVENTS,

        SUBSCRIBED
    }

}
