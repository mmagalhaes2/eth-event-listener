package com.math.cleanarchex.infra.driven.blockchainProvider.dto.message;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContractEvent extends AbstractMessage<ContractEventDetails> {

    public static final String TYPE = "CONTRACT_EVENT";

    public ContractEvent(ContractEventDetails details) {
        super(details.getId(), TYPE, details);
    }
}
