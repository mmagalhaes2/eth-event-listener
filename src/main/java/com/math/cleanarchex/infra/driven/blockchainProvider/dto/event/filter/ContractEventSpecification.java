package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
public class ContractEventSpecification implements Serializable {

    private String eventName;

    private List<ParameterDefinition> indexedParameterDefinitions = new ArrayList<>();

    private List<ParameterDefinition> nonIndexedParameterDefinitions = new ArrayList<>();
}
