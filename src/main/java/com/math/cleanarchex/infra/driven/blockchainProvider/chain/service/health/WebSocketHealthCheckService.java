package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainException;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health.strategy.ReconnectionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.EventeumValueMonitor;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.EventStoreService;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.websocket.EventeumWebSocketService;
import org.web3j.protocol.websocket.WebSocketClient;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class WebSocketHealthCheckService extends NodeHealthCheckService {

    private WebSocketClient webSocketClient;

    public WebSocketHealthCheckService(Web3jService web3jService,
                                       BlockchainService blockchainService,
                                       BlockSubscriptionStrategy blockSubscription,
                                       ReconnectionStrategy failureListener,
                                       SubscriptionService subscriptionService,
                                       EventeumValueMonitor valueMonitor,
                                       EventStoreService eventStoreService,
                                       Integer syncingThreshold,
//                                       ScheduledThreadPoolExecutor taskScheduler,
                                       Long healthCheckPollInterval
    ) {
        super(blockchainService, blockSubscription, failureListener, subscriptionService,
                valueMonitor, eventStoreService, syncingThreshold,
//                taskScheduler,
                healthCheckPollInterval);

        if (web3jService instanceof EventeumWebSocketService) {
            this.webSocketClient = ((EventeumWebSocketService) web3jService).getWebSocketClient();
        } else {
            throw new BlockchainException(
                    "Non web socket service passed to WebSocketHealthCheckService");
        }

    }

    @Override
    protected boolean isSubscribed() {
        return super.isSubscribed() && webSocketClient.isOpen();
    }
}
