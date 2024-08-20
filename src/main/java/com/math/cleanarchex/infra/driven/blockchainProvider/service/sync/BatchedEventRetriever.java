package com.math.cleanarchex.infra.driven.blockchainProvider.service.sync;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container.ChainServicesContainer;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.settings.EventeumSettings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class BatchedEventRetriever implements EventRetriever {


    private ChainServicesContainer servicesContainer;

    private EventeumSettings settings;

    @Override
    public void retrieveEvents(ContractEventFilter eventFilter,
                               BigInteger startBlock,
                               BigInteger endBlock,
                               Consumer<List<ContractEventDetails>> eventConsumer) {

        BigInteger batchStartBlock = startBlock;

        while (batchStartBlock.compareTo(endBlock) < 0) {
            BigInteger batchEndBlock;

            if (batchStartBlock.add(settings.getSyncBatchSize()).compareTo(endBlock) >= 0) {
                batchEndBlock = endBlock;
            } else {
                batchEndBlock = batchStartBlock.add(settings.getSyncBatchSize());
            }

            final List<ContractEventDetails> events = servicesContainer
                    .getNodeServices(eventFilter.getNode())
                    .getBlockchainService()
                    .retrieveEvents(eventFilter, batchStartBlock, batchEndBlock);

            eventConsumer.accept(events);

            batchStartBlock = batchEndBlock.add(BigInteger.ONE);
        }
    }
}
