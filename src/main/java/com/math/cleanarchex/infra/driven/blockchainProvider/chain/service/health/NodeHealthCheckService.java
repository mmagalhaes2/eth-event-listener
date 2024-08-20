package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.health.strategy.ReconnectionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.EventeumValueMonitor;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.EventStoreService;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NodeHealthCheckService {

    private final BlockchainService blockchainService;

    private final BlockSubscriptionStrategy blockSubscription;
    private final ReconnectionStrategy reconnectionStrategy;
    private final SubscriptionService subscriptionService;
    private final AtomicLong currentBlock;
    private final AtomicInteger syncing;
    private final AtomicInteger nodeStatusGauge;
    private final EventStoreService eventStoreService;
    private final Integer syncingThreshold;
    //    private final ScheduledThreadPoolExecutor taskScheduler;
    private final Long healthCheckPollInterval;
    private NodeStatus nodeStatus;

    public NodeHealthCheckService(BlockchainService blockchainService,
                                  BlockSubscriptionStrategy blockSubscription,
                                  ReconnectionStrategy reconnectionStrategy,
                                  SubscriptionService subscriptionService,
                                  EventeumValueMonitor valueMonitor,
                                  EventStoreService eventStoreService,
                                  Integer syncingThreshold,
//                                  ScheduledThreadPoolExecutor taskScheduler,
                                  Long healthCheckPollInterval) {
        this.eventStoreService = eventStoreService;
        this.blockchainService = blockchainService;
        this.blockSubscription = blockSubscription;
        this.reconnectionStrategy = reconnectionStrategy;
        this.subscriptionService = subscriptionService;
        this.syncingThreshold = syncingThreshold;
//        this.taskScheduler = taskScheduler;
        this.healthCheckPollInterval = healthCheckPollInterval;
        nodeStatus = NodeStatus.SUBSCRIBED;

        currentBlock = valueMonitor.monitor("currentBlock", blockchainService.getNodeName(), new
                AtomicLong(0));
        nodeStatusGauge = valueMonitor.monitor("status", blockchainService.getNodeName(), new
                AtomicInteger(NodeStatus.SUBSCRIBED.ordinal()));
        syncing = valueMonitor.monitor("syncing", blockchainService.getNodeName(), new
                AtomicInteger(0));

    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Starting healthcheck scheduler");
//        taskScheduler.scheduleWithFixedDelay(this::checkHealth, 0, healthCheckPollInterval, TimeUnit.MILLISECONDS);
    }

    @Scheduled(fixedDelayString = "${healthcheck.poll.interval:5000}")
    public void checkHealth() {
        try {
            log.trace("Checking health");

            final NodeStatus statusAtStart = nodeStatus;

            if (isNodeConnected() && (isSubscribed()
                    || subscriptionService.getState() == SubscriptionService.SubscriptionServiceState.SYNCING_EVENTS)) {
                log.trace("Node connected");

                if (nodeStatus == NodeStatus.DOWN) {
                    log.info("Node {} has come back up.", blockchainService.getNodeName());
                }

            } else if (subscriptionService.getState() == SubscriptionService.SubscriptionServiceState.SUBSCRIBED) {
                log.error("Node {} is down or unsubscribed!!", blockchainService.getNodeName());
                nodeStatus = NodeStatus.DOWN;

                if (statusAtStart != NodeStatus.DOWN) {
                    blockSubscription.unsubscribe();
                }

                doReconnectAndSubscribe();
            } else if (subscriptionService.getState() == SubscriptionService.SubscriptionServiceState.SYNCING_EVENTS) {
                log.error("Node {} is down!!", blockchainService.getNodeName());
                nodeStatus = NodeStatus.DOWN;

                doReconnect();
            }

            nodeStatusGauge.set(nodeStatus.ordinal());
        } catch (Throwable t) {
            log.error("An error occured during the check health / recovery process...Will retry at next poll", t);
        }
    }

    protected boolean isNodeConnected() {
        try {
            currentBlock.set(blockchainService.getCurrentBlockNumber().longValue());

            if (currentBlock.longValue() <= syncingThreshold + getLatestBlockForNode().getNumber().longValue()) {
                syncing.set(0);
            } else {
                syncing.set(1);
            }
        } catch (Throwable t) {
            log.error("Get latest block failed with exception on node " + blockchainService.getNodeName(), t);

            return false;
        }

        return true;
    }

    protected boolean isSubscribed() {
        return blockSubscription.isSubscribed();
    }

    private void doReconnect() {
        reconnectionStrategy.reconnect();
    }

    private void doReconnectAndSubscribe() {
        reconnectionStrategy.reconnect();

        if (isNodeConnected()) {
            nodeStatus = NodeStatus.CONNECTED;
            doResubscribe();
        }
    }

    private void doResubscribe() {
        reconnectionStrategy.resubscribe();

        nodeStatus = isSubscribed() ? NodeStatus.CONNECTED : NodeStatus.DOWN;
    }

    private LatestBlock getLatestBlockForNode() {
        return eventStoreService.getLatestBlock(
                blockchainService.getNodeName()).orElseGet(() -> {
            final LatestBlock latestBlock = new LatestBlock();
            latestBlock.setNumber(BigInteger.ZERO);
            return latestBlock;
        });
    }

    private enum NodeStatus {
        CONNECTED,
        SUBSCRIBED,
        DOWN
    }

}
