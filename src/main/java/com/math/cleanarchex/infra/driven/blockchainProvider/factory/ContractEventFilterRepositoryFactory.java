package com.math.cleanarchex.infra.driven.blockchainProvider.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import org.springframework.data.repository.CrudRepository;

public interface ContractEventFilterRepositoryFactory {

    CrudRepository<ContractEventFilter, String> build();
}
