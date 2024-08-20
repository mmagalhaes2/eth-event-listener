package com.math.cleanarchex.domain.entities;

import java.util.List;

public record EventSpecification(String contractAddress, String name, List<Parameter> parameters) {
}
