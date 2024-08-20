package com.math.cleanarchex.infra.driven.blockchainProvider.dto.message;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContractEventFilterRemoved extends AbstractMessage<ContractEventFilter> {

    public static final String TYPE = "EVENT_FILTER_REMOVED";

    public ContractEventFilterRemoved(ContractEventFilter filter) {
        super(filter.getId(), TYPE, filter);
    }
}

