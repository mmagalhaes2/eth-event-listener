package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.mixin.PageMixIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

/**
 * Configures the jackson ObjectMapper bean.
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Page.class, PageMixIn.class);

        return mapper;
    }
}
