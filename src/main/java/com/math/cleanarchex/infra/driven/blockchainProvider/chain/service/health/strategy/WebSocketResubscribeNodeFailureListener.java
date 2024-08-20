package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health.strategy;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.websocket.WebSocketReconnectionManager;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.websocket.WebSocketClient;

@Slf4j
public class WebSocketResubscribeNodeFailureListener extends ResubscribingReconnectionStrategy {

    private final WebSocketReconnectionManager reconnectionManager;
    private final WebSocketClient client;

    public WebSocketResubscribeNodeFailureListener(SubscriptionService subscriptionService,
                                                   BlockSubscriptionStrategy blockSubscription,
                                                   WebSocketReconnectionManager reconnectionManager,
                                                   WebSocketClient client) {
        super(subscriptionService, blockSubscription);

        this.reconnectionManager = reconnectionManager;
        this.client = client;
    }

    @Override
    public void reconnect() {
        log.info("Reconnecting web socket because of {} node failure", getBlockSubscriptionStrategy().getNodeName());
        reconnectionManager.reconnect(client);
    }
}
