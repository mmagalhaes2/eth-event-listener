package com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db.repository;

import com.math.cleanarchex.infra.driven.blockchainProvider.factory.EventStoreFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("latestBlockRepository")
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
@ConditionalOnMissingBean(EventStoreFactory.class)
public interface LatestBlockRepository extends CrudRepository<LatestBlock, String> {
}
