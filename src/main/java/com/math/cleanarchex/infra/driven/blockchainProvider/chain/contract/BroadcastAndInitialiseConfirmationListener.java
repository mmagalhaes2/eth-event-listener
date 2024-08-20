package com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.BlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.EventConfirmationBlockListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.TransactionReceipt;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy.BlockSubscriptionStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.Node;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.NodeSettings;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventStatus;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;


@Component
@AllArgsConstructor
@Slf4j
public class BroadcastAndInitialiseConfirmationListener implements ContractEventListener {

    private ChainServicesContainer chainServicesContainer;
    private BlockchainEventBroadcaster eventBroadcaster;
    private NodeSettings nodeSettings;

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        if (eventDetails.getStatus() == ContractEventStatus.UNCONFIRMED) {

            final BlockSubscriptionStrategy blockSubscription = getBlockSubscriptionStrategy(eventDetails);
            final Node node = nodeSettings.getNode(eventDetails.getNodeName());

            if (shouldInstantlyConfirm(eventDetails)) {
                eventDetails.setStatus(ContractEventStatus.CONFIRMED);
                eventBroadcaster.broadcastContractEvent(eventDetails);

                return;
            }

            log.info("Registering an EventConfirmationBlockListener for event: {}", eventDetails.getId());
            blockSubscription.addBlockListener(createEventConfirmationBlockListener(eventDetails, node));
        }

        eventBroadcaster.broadcastContractEvent(eventDetails);
    }

    protected BlockListener createEventConfirmationBlockListener(ContractEventDetails eventDetails, Node node) {
        return new EventConfirmationBlockListener(eventDetails,
                getBlockchainService(eventDetails), getBlockSubscriptionStrategy(eventDetails), eventBroadcaster, node);
    }

    private BlockchainService getBlockchainService(ContractEventDetails eventDetails) {
        return chainServicesContainer.getNodeServices(
                eventDetails.getNodeName()).getBlockchainService();
    }

    private BlockSubscriptionStrategy getBlockSubscriptionStrategy(ContractEventDetails eventDetails) {
        return chainServicesContainer.getNodeServices(
                eventDetails.getNodeName()).getBlockSubscriptionStrategy();
    }

    private boolean shouldInstantlyConfirm(ContractEventDetails eventDetails) {
        final BlockchainService blockchainService = getBlockchainService(eventDetails);
        final Node node = nodeSettings.getNode(blockchainService.getNodeName());
        BigInteger currentBlock = blockchainService.getCurrentBlockNumber();
        BigInteger waitBlocks = node.getBlocksToWaitForConfirmation();

        return currentBlock.compareTo(eventDetails.getBlockNumber().add(waitBlocks)) >= 0
                && isTransactionStillInBlock(
                eventDetails.getTransactionHash(), eventDetails.getBlockHash(), blockchainService);
    }

    private boolean isTransactionStillInBlock(String txHash, String blockHash, BlockchainService blockchainService) {
        final TransactionReceipt receipt = blockchainService.getTransactionReceipt(txHash);

        return receipt != null && receipt.getBlockHash().equals(blockHash);
    }
}
