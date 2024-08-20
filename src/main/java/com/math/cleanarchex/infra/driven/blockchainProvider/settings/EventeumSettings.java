package com.math.cleanarchex.infra.driven.blockchainProvider.settings;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@Data
public class EventeumSettings {

    private boolean bytesToAscii;

    private BigInteger syncBatchSize;

    public EventeumSettings(@Value("${broadcaster.bytesToAscii:false}") boolean bytesToAscii,
                            @Value("${ethereum.sync.batchSize:100000}") String syncBatchSize) {
        this.bytesToAscii = bytesToAscii;
        this.syncBatchSize = new BigInteger(syncBatchSize);
    }
}
