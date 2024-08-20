package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.EventBroadcasterWrapper;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.HttpBlockchainEventBroadcaster;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.HttpBroadcasterSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * Spring bean configuration for the BlockchainEventBroadcaster.
 * <p>
 * Registers a broadcaster bean based on the value of the broadcaster.type property.
 */
@Configuration
public class BlockchainEventBroadcasterConfiguration {

    private static final String EXPIRATION_PROPERTY = "${broadcaster.cache.expirationMillis}";
    private static final String BROADCASTER_PROPERTY = "broadcaster.type";
    private static final String ENABLE_BLOCK_NOTIFICATIONS = "${broadcaster.enableBlockNotifications:true}";

    private Long onlyOnceCacheExpirationTime;
    private boolean enableBlockNotifications;

    @Autowired
    public BlockchainEventBroadcasterConfiguration(@Value(EXPIRATION_PROPERTY) Long onlyOnceCacheExpirationTime, @Value(ENABLE_BLOCK_NOTIFICATIONS) boolean enableBlockNotifications) {
        this.onlyOnceCacheExpirationTime = onlyOnceCacheExpirationTime;
        this.enableBlockNotifications = enableBlockNotifications;
    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(name= BROADCASTER_PROPERTY, havingValue="KAFKA")
//    public BlockchainEventBroadcaster kafkaBlockchainEventBroadcaster(KafkaTemplate<String, EventeumMessage> kafkaTemplate,
//                                                                      KafkaSettings kafkaSettings,
//                                                                      CrudRepository<ContractEventFilter, String> filterRepository) {
//        final BlockchainEventBroadcaster broadcaster =
//                new KafkaBlockchainEventBroadcaster(kafkaTemplate, kafkaSettings, filterRepository);
//
//        return onlyOnceWrap(broadcaster);
//    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = BROADCASTER_PROPERTY, havingValue = "HTTP")
    public BlockchainEventBroadcaster httpBlockchainEventBroadcaster(HttpBroadcasterSettings settings, @Qualifier("eternalRetryTemplate") RetryTemplate retryTemplate) {
        final BlockchainEventBroadcaster broadcaster =
                new HttpBlockchainEventBroadcaster(settings, retryTemplate);

        return onlyOnceWrap(broadcaster);
    }

    private BlockchainEventBroadcaster onlyOnceWrap(BlockchainEventBroadcaster toWrap) {
        return new EventBroadcasterWrapper(onlyOnceCacheExpirationTime, toWrap, enableBlockNotifications);
    }
}
