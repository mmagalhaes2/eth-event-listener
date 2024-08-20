package com.math.cleanarchex.infra.driven.blockchainProvider.dto.message;

import com.math.cleanarchex.infra.driven.blockchainProvider.model.TransactionMonitoringSpec;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionMonitorAdded extends AbstractMessage<TransactionMonitoringSpec> {

    public static final String TYPE = "TRANSACTION_MONITOR_ADDED";

    public TransactionMonitorAdded(TransactionMonitoringSpec spec) {
        super(spec.getId(), TYPE, spec);
    }
}
