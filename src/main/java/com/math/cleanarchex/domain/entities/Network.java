package com.math.cleanarchex.domain.entities;

import com.math.cleanarchex.domain.entities.enums.ConnectionTypeEnum;


public record Network(String name, String endpoint, ConnectionTypeEnum connectionType) {
}
