/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.BlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.internal.EventeumEventBroadcaster;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.sync.EventSyncService;
import com.math.cleanarchex.infra.handler.exception.NotFoundException;
import com.math.cleanarchex.infra.persistence.ContractEventFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultSubscriptionService implements SubscriptionService {

    private final ChainServicesContainer chainServices;

    private final ContractEventFilterRepository eventFilterRepository;

    private final EventeumEventBroadcaster eventeumEventBroadcaster;

    private final List<BlockListener> blockListeners;

    private final Map<String, ContractEventFilter> filterSubscriptions = new HashMap<>();
    private final EventSyncService eventSyncService;
    private RetryTemplate retryTemplate;
    private SubscriptionServiceState state = SubscriptionServiceState.UNINITIALISED;


    public void init(List<ContractEventFilter> initFilters) {

        if (initFilters != null && !initFilters.isEmpty()) {
            final List<ContractEventFilter> filtersWithStartBlock = initFilters
                    .stream()
                    .filter(filter -> filter.getStartBlock() != null)
                    .collect(Collectors.toList());

            if (!filtersWithStartBlock.isEmpty()) {
                state = SubscriptionServiceState.SYNCING_EVENTS;
                eventSyncService.sync(filtersWithStartBlock);
            }
        }

        chainServices.getNodeNames().forEach(nodeName -> subscribeToNewBlockEvents(
                chainServices.getNodeServices(nodeName).getBlockSubscriptionStrategy(), blockListeners));

        state = SubscriptionServiceState.SUBSCRIBED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContractEventFilter registerContractEventFilter(ContractEventFilter filter, boolean broadcast) {
        return doRegisterContractEventFilter(filter, broadcast);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public CompletableFuture<ContractEventFilter> registerContractEventFilterWithRetries(ContractEventFilter filter, boolean broadcast) {
        return CompletableFuture.completedFuture(retryTemplate.execute((context) -> doRegisterContractEventFilter(filter, broadcast)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContractEventFilter> listContractEventFilters() {
        return new ArrayList<>(filterSubscriptions.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterContractEventFilter(String filterId) throws NotFoundException {
        unregisterContractEventFilter(filterId, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterContractEventFilter(String filterId, boolean broadcast) throws NotFoundException {
        final ContractEventFilter filterToUnregister = getRegisteredFilter(filterId);

        if (filterToUnregister == null) {
            throw new NotFoundException(String.format("Filter with id %s, doesn't exist", filterId));
        }

        deleteContractEventFilter(filterToUnregister);
        removeFilterSubscription(filterId);

        if (broadcast) {
            broadcastContractEventFilterRemoved(filterToUnregister);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribeToAllSubscriptions(String nodeName) {
        filterSubscriptions
                .entrySet()
                .removeIf(entry -> entry.getValue().getNode().equals(nodeName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubscriptionServiceState getState() {
        return state;
    }

    private ContractEventFilter doRegisterContractEventFilter(ContractEventFilter filter, boolean broadcast) {
        try {
            populateIdIfMissing(filter);

            if (!isFilterRegistered(filter)) {
                filterSubscriptions.put(filter.getId(), filter);

                //TODO start block replay

                saveContractEventFilter(filter);

                if (broadcast) {
                    broadcastContractEventFilterAdded(filter);
                }

                return filter;
            } else {
                log.info("Already registered contract event filter with id: {}", filter.getId());
                return getRegisteredFilter(filter.getId());
            }
        } catch (Exception e) {
            log.error("Error registering filter {}", filter.getId(), e);
            throw e;
        }
    }

    private void subscribeToNewBlockEvents(BlockSubscriptionStrategy subscriptionStrategy, List<BlockListener> blockListeners) {
        blockListeners.forEach(subscriptionStrategy::addBlockListener);
        subscriptionStrategy.subscribe();
    }

    private ContractEventFilter saveContractEventFilter(ContractEventFilter contractEventFilter) {
        return eventFilterRepository.save(contractEventFilter);
    }

    private void deleteContractEventFilter(ContractEventFilter contractEventFilter) {
        eventFilterRepository.deleteById(contractEventFilter.getId());
    }

    private void broadcastContractEventFilterAdded(ContractEventFilter filter) {
        eventeumEventBroadcaster.broadcastEventFilterAdded(filter);
    }

    private void broadcastContractEventFilterRemoved(ContractEventFilter filter) {
        eventeumEventBroadcaster.broadcastEventFilterRemoved(filter);
    }

    private boolean isFilterRegistered(ContractEventFilter contractEventFilter) {
        return (getRegisteredFilter(contractEventFilter.getId()) != null);
    }

    private ContractEventFilter getRegisteredFilter(String filterId) {
        return filterSubscriptions.get(filterId);
    }

    private void removeFilterSubscription(String filterId) {
        filterSubscriptions.remove(filterId);
    }

    private void populateIdIfMissing(ContractEventFilter filter) {
        if (filter.getId() == null) {
            filter.setId(UUID.randomUUID().toString());
        }
    }
}
