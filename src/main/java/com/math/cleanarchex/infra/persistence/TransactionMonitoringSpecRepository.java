package com.math.cleanarchex.infra.persistence;

import com.math.cleanarchex.infra.driven.blockchainProvider.factory.ContractEventFilterRepositoryFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnMissingBean(ContractEventFilterRepositoryFactory.class)
public interface TransactionMonitoringSpecRepository extends MongoRepository<TransactionMonitoringSpec, String> {
}
