package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractEventParameter<T extends Serializable> implements EventParameter<T> {

    private String type;

    private T value;
}
