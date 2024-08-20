package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.strategy;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block.BlockNumberService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.wrapper.Web3jBlock;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.AsyncTaskService;
import com.math.cleanarchex.infra.driven.blockchainProvider.utils.JSON;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;

@Slf4j
public class PollingBlockSubscriptionStrategy extends AbstractBlockSubscriptionStrategy<EthBlock> {

    public PollingBlockSubscriptionStrategy(Web3j web3j,
                                            String nodeName,
                                            AsyncTaskService asyncService,
                                            BlockNumberService blockNumberService) {
        super(web3j, nodeName, asyncService, blockNumberService);
    }

    @Override
    public Disposable subscribe() {

        final BigInteger startBlock = getStartBlock();

        log.info("Starting block polling, from block {}", startBlock);

        final DefaultBlockParameter blockParam = DefaultBlockParameter.valueOf(startBlock);

        blockSubscription = web3j
                .replayPastAndFutureBlocksFlowable(blockParam, true)
                .doOnError((error) -> onError(blockSubscription, error))
                .subscribe(this::triggerListeners, (error) -> onError(blockSubscription, error));

        return blockSubscription;
    }

    @Override
    Block convertToEventeumBlock(EthBlock blockObject) {
        //Infura is sometimes returning null blocks...just ignore in this case.
        if (blockObject == null || blockObject.getBlock() == null) {
            return null;
        }

        try {
            return new Web3jBlock(blockObject.getBlock(), nodeName);
        } catch (Throwable t) {
            log.error("Error converting block: " + JSON.stringify(blockObject), t);
            throw t;
        }
    }
}
