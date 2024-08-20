package com.math.cleanarchex.infra.driven.blockchainProvider.model;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import io.reactivex.disposables.Disposable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class FilterSubscription {

    private ContractEventFilter filter;

    private Disposable subscription;

    private BigInteger startBlock;

    public FilterSubscription(ContractEventFilter filter, Disposable subscription) {
        this.filter = filter;
        this.subscription = subscription;
    }
}
