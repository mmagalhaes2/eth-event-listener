package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NonIndexedParameterCorrelationIdStrategy extends ParameterCorrelationIdStrategy {

    public static final String TYPE = "NON_INDEXED_PARAMETER";

    public NonIndexedParameterCorrelationIdStrategy(int parameterIndex) {
        super(TYPE, parameterIndex);
    }

    @Override
    public String getCorrelationId(ContractEventDetails contractEvent) {
        return contractEvent
                .getNonIndexedParameters()
                .get(getParameterIndex())
                .getValueString();
    }
}
