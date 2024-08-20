package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonTypeIdResolver(ParameterTypeIdResolver.class)
public interface EventParameter<T extends Serializable> extends Serializable {
    String getType();

    T getValue();

    @JsonIgnore
    String getValueString();
}
