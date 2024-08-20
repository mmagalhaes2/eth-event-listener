package com.math.cleanarchex.domain.entities;

public record Parameter(Integer position, ParameterType type, Boolean isIndexed) implements Comparable<Parameter> {

    @Override
    public int compareTo(Parameter o) {
        return this.position().compareTo(o.position());
    }
}
