package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IndexedParameterCorrelationIdStrategy extends ParameterCorrelationIdStrategy {

    public static final String TYPE = "INDEXED_PARAMETER";

    public IndexedParameterCorrelationIdStrategy(int parameterIndex) {
        super(TYPE, parameterIndex);
    }

    @Override
    public String getCorrelationId(ContractEventDetails contractEvent) {
        return contractEvent
                .getIndexedParameters()
                .get(getParameterIndex())
                .getValueString();
    }
}
