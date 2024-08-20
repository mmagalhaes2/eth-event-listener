package com.math.cleanarchex.infra.driven.blockchainProvider.service.sync;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;

import java.util.List;

public interface EventSyncService {

    void sync(List<ContractEventFilter> filters);

}
