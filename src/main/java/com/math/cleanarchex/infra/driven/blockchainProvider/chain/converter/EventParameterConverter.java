package com.math.cleanarchex.infra.driven.blockchainProvider.chain.converter;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.parameter.EventParameter;

public interface EventParameterConverter<T> {

    EventParameter convert(T toConvert);
}
