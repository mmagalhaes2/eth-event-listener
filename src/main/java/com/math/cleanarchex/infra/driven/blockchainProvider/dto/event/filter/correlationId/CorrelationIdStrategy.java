package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IndexedParameterCorrelationIdStrategy.class,
                name = IndexedParameterCorrelationIdStrategy.TYPE),
        @JsonSubTypes.Type(value = NonIndexedParameterCorrelationIdStrategy.class,
                name = NonIndexedParameterCorrelationIdStrategy.TYPE)})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CorrelationIdStrategy {
    String getType();

    @JsonIgnore
    String getCorrelationId(ContractEventDetails contractEvent);
}
