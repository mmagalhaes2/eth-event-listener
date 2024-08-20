package com.math.cleanarchex.domain.entities;

import com.math.cleanarchex.domain.entities.enums.CorrelationStrategyEnum;

public record CorrelationStrategy(CorrelationStrategyEnum type, Integer parameterIndex) {
}
