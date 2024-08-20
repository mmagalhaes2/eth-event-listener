package com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.util.BloomFilterUtil;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.AsyncTaskService;
import com.math.cleanarchex.infra.driven.blockchainProvider.utils.ExecutorNameFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultContractEventProcessor implements ContractEventProcessor {

    private static final String EVENT_EXECUTOR_NAME = "EVENT";

    private ChainServicesContainer chainServices;

    private AsyncTaskService asyncTaskService;

    private List<ContractEventListener> contractEventListeners;

    @Override
    public void processLogsInBlock(Block block, List<ContractEventFilter> contractEventFilters) {
        asyncTaskService.executeWithCompletableFuture(ExecutorNameFactory.build(EVENT_EXECUTOR_NAME, block.getNodeName()), () -> {
            final BlockchainService blockchainService = getBlockchainService(block.getNodeName());

            contractEventFilters
                    .forEach(filter -> processLogsForFilter(filter, block, blockchainService));
        }).join();
    }

    @Override
    public void processContractEvent(ContractEventDetails contractEventDetails) {
        asyncTaskService.executeWithCompletableFuture(
                ExecutorNameFactory.build(EVENT_EXECUTOR_NAME, contractEventDetails.getNodeName()),
                () -> triggerListeners(contractEventDetails)).join();
    }

    private void processLogsForFilter(ContractEventFilter filter,
                                      Block block,
                                      BlockchainService blockchainService) {

        if (block.getNodeName().equals(filter.getNode())
                && isEventFilterInBloomFilter(filter, block.getLogsBloom())) {
            blockchainService
                    .getEventsForFilter(filter, block.getNumber())
                    .forEach(event -> {
                        event.setTimestamp(block.getTimestamp());
                        triggerListeners(event);
                    });
        }
    }

    private boolean isEventFilterInBloomFilter(ContractEventFilter filter, String logsBloom) {
        final BloomFilterUtil.BloomFilterBits bloomBits = BloomFilterUtil.getBloomBits(filter);

        return BloomFilterUtil.bloomFilterMatch(logsBloom, bloomBits);
    }

    private BlockchainService getBlockchainService(String nodeName) {
        return chainServices.getNodeServices(nodeName).getBlockchainService();
    }

    private void triggerListeners(ContractEventDetails contractEvent) {
        contractEventListeners.forEach(
                listener -> triggerListener(listener, contractEvent));
    }

    private void triggerListener(ContractEventListener listener, ContractEventDetails contractEventDetails) {
        try {
            listener.onEvent(contractEventDetails);
        } catch (Throwable t) {
            log.error(String.format(
                    "An error occurred when processing contractEvent with id %s", contractEventDetails.getId()), t);
            throw t;
        }
    }
}
