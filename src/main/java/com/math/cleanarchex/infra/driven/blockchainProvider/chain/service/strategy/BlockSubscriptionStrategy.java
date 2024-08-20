package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.block.BlockListener;
import io.reactivex.disposables.Disposable;

public interface BlockSubscriptionStrategy {

    String getNodeName();

    Disposable subscribe();

    void unsubscribe();

    void addBlockListener(BlockListener blockListener);

    void removeBlockListener(BlockListener blockListener);

    boolean isSubscribed();
}
