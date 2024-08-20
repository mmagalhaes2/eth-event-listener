package com.math.cleanarchex.infra.driven.blockchainProvider.chain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BlockchainServiceRegistrar.class)
public class BaseChainConfiguration {

}
