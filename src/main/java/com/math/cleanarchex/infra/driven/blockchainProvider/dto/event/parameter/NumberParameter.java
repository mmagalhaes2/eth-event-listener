package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class NumberParameter extends AbstractEventParameter<BigInteger> {

    public NumberParameter(String type, BigInteger value) {
        super(type, value);
    }

    @Override
    public String getValueString() {
        return getValue().toString();
    }
}
