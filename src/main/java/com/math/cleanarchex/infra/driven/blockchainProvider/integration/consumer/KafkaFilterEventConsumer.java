package com.math.cleanarchex.infra.driven.blockchainProvider.integration.consumer;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.message.*;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.KafkaSettings;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.SubscriptionService;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.TransactionMonitoringService;
import com.math.cleanarchex.infra.handler.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A FilterEventConsumer that consumes ContractFilterEvents messages from a Kafka topic.
 * <p>
 * The topic to be consumed from can be configured via the kafka.topic.contractEvents property.
 */
@Component
public class KafkaFilterEventConsumer implements EventeumInternalEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaFilterEventConsumer.class);

    private final Map<String, Consumer<EventeumMessage>> messageConsumers;

    @Autowired
    public KafkaFilterEventConsumer(SubscriptionService subscriptionService,
                                    TransactionMonitoringService transactionMonitoringService,
                                    KafkaSettings kafkaSettings) {

        messageConsumers = new HashMap<>();
        messageConsumers.put(ContractEventFilterAdded.TYPE, (message) -> {
            subscriptionService.registerContractEventFilter(
                    (ContractEventFilter) message.getDetails(), false);
        });

        messageConsumers.put(ContractEventFilterRemoved.TYPE, (message) -> {
            try {
                subscriptionService.unregisterContractEventFilter(
                        ((ContractEventFilter) message.getDetails()).getId(), false);
            } catch (NotFoundException e) {
                logger.debug("Received filter removed message but filter doesn't exist. (We probably sent message)");
            }
        });

        messageConsumers.put(TransactionMonitorAdded.TYPE, (message) -> {
            transactionMonitoringService.registerTransactionsToMonitor(
                    (TransactionMonitoringSpec) message.getDetails(), false);
        });

        messageConsumers.put(TransactionMonitorRemoved.TYPE, (message) -> {
            try {
                transactionMonitoringService.stopMonitoringTransactions(
                        ((TransactionMonitoringSpec) message.getDetails()).getId(), false);
            } catch (NotFoundException e) {
                logger.debug("Received transaction monitor removed message but monitor doesn't exist. (We probably sent message)");
            }
        });
    }

    @Override
//    @KafkaListener(topics = "#{eventeumKafkaSettings.eventeumEventsTopic}", groupId = "#{eventeumKafkaSettings.groupId}",
//            containerFactory = "eventeumKafkaListenerContainerFactory")
    public void onMessage(EventeumMessage message) {
        final Consumer<EventeumMessage> consumer = messageConsumers.get(message.getType());

        if (consumer == null) {
            logger.error(String.format("No consumer for message type %s!", message.getType()));
            return;
        }

        consumer.accept(message);
    }
}
