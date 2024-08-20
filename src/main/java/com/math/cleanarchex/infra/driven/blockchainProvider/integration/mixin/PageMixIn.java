package com.math.cleanarchex.infra.driven.blockchainProvider.integration.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SimplePageImpl.class)
public interface PageMixIn {
}
