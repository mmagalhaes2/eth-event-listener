package com.math.cleanarchex.infra.driven.blockchainProvider.integration.consumer;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.message.EventeumMessage;

/**
 * A consumer for internal Eventeum messages sent from a different instance.
 */
public interface EventeumInternalEventConsumer {
    void onMessage(EventeumMessage<?> message);
}
