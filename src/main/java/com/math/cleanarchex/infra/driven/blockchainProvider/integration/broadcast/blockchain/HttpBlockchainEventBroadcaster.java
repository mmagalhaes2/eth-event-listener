package com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.blockchain;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.block.BlockDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.broadcast.BroadcastException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * A BlockchainEventBroadcaster that broadcasts the events via a http post.
 * <p>
 * The url to post to for block and contract events can be configured via the
 * broadcast.http.contractEvents and broadcast.http.blockEvents properties.
 */
public class HttpBlockchainEventBroadcaster implements BlockchainEventBroadcaster {

    private final HttpBroadcasterSettings settings;

    private final RestTemplate restTemplate;

    private final RetryTemplate retryTemplate;

    public HttpBlockchainEventBroadcaster(HttpBroadcasterSettings settings, RetryTemplate retryTemplate) {
        this.settings = settings;

        restTemplate = new RestTemplate();
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        retryTemplate.execute((context) -> {
            final ResponseEntity<Void> response =
                    restTemplate.postForEntity(settings.getBlockEventsUrl(), block, Void.class);

            checkForSuccessResponse(response);
            return null;
        });
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        retryTemplate.execute((context) -> {
            final ResponseEntity<Void> response =
                    restTemplate.postForEntity(settings.getContractEventsUrl(), eventDetails, Void.class);

            checkForSuccessResponse(response);
            return null;
        });
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {

    }

    private void checkForSuccessResponse(ResponseEntity<Void> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new BroadcastException(
                    String.format("Received a %s response when broadcasting via http", response.getStatusCode()));
        }
    }
}
