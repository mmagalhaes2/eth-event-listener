package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class ParameterCorrelationIdStrategy implements CorrelationIdStrategy {
    private int parameterIndex;

    private String type;

    protected ParameterCorrelationIdStrategy(String type, int parameterIndex) {
        this.type = type;
        this.parameterIndex = parameterIndex;
    }
}
