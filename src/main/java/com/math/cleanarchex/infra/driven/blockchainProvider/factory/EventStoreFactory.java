package com.math.cleanarchex.infra.driven.blockchainProvider.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.SaveableEventStore;

public interface EventStoreFactory {

    SaveableEventStore build();
}
