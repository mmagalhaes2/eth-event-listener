
package com.math.cleanarchex.infra.driven.blockchainProvider.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;

import java.util.List;

public interface ContractEventFilterFactory {

    List<ContractEventFilter> build();
}
