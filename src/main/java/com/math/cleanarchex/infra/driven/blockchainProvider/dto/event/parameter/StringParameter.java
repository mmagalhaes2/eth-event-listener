package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StringParameter extends AbstractEventParameter<String> {

    public StringParameter(String type, String value) {
        super(type, value);
    }

    @Override
    public String getValueString() {
        return getValue();
    }
}
