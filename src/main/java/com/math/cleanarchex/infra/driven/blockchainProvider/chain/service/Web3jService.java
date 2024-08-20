package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract.ContractEventListener;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.ContractEventDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Block;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.TransactionReceipt;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.wrapper.Web3jBlock;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.wrapper.Web3jTransactionReceipt;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.util.Web3jUtil;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventSpecification;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.FilterSubscription;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.AsyncTaskService;
import com.math.cleanarchex.infra.driven.blockchainProvider.utils.ExecutorNameFactory;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
public class Web3jService implements BlockchainService {

    private static final String EVENT_EXECUTOR_NAME = "EVENT";
    private final ContractEventDetailsFactory eventDetailsFactory;
    private final AsyncTaskService asyncTaskService;
    @Getter
    private String nodeName;
    @Getter
    @Setter
    private Web3j web3j;

    public Web3jService(String nodeName,
                        Web3j web3j,
                        ContractEventDetailsFactory eventDetailsFactory,
                        AsyncTaskService asyncTaskService) {
        this.nodeName = nodeName;
        this.web3j = web3j;
        this.eventDetailsFactory = eventDetailsFactory;
        this.asyncTaskService = asyncTaskService;
    }

    @Override
    public List<ContractEventDetails> retrieveEvents(ContractEventFilter eventFilter,
                                                     BigInteger startBlock,
                                                     BigInteger endBlock) {
        final ContractEventSpecification eventSpec = eventFilter.getEventSpecification();

        final EthFilter ethFilter = new EthFilter(new DefaultBlockParameterNumber(startBlock),
                new DefaultBlockParameterNumber(endBlock), eventFilter.getContractAddress());


        if (eventFilter.getEventSpecification() != null) {
            ethFilter.addSingleTopic(Web3jUtil.getSignature(eventSpec));
        }

        try {
            final EthLog logs = web3j.ethGetLogs(ethFilter).send();
            return logs.getLogs()
                    .stream()
                    .map(logResult -> eventDetailsFactory.createEventDetails(eventFilter, (Log) logResult.get()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new BlockchainException("Error obtaining logs", e);
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public FilterSubscription registerEventListener(ContractEventFilter eventFilter,
                                                    ContractEventListener eventListener,
                                                    BigInteger startBlock,
                                                    BigInteger endBlock,
                                                    Optional<Runnable> onCompletion) {
        log.debug("Registering event filter for event: {}", eventFilter.getId());
        final ContractEventSpecification eventSpec = eventFilter.getEventSpecification();

        final EthFilter ethFilter = new EthFilter(new DefaultBlockParameterNumber(startBlock),
                new DefaultBlockParameterNumber(endBlock), eventFilter.getContractAddress());


        if (eventFilter.getEventSpecification() != null) {
            ethFilter.addSingleTopic(Web3jUtil.getSignature(eventSpec));
        }

        final Flowable<Log> flowable = web3j
                .ethLogFlowable(ethFilter)
                .doOnComplete(() -> {
                    onCompletion.ifPresent(Runnable::run);
                });

        final Disposable sub = flowable.subscribe(theLog -> {
            asyncTaskService.execute(ExecutorNameFactory.build(EVENT_EXECUTOR_NAME, eventFilter.getNode()), () -> {
                log.debug("Dispatching log: {}", theLog);

                //Check signatures match
                if (ethFilter.getTopics() == null
                        || ethFilter.getTopics().isEmpty()
                        || ethFilter.getTopics().get(0).getValue().equals(theLog.getTopics().get(0))) {
                    eventListener.onEvent(
                            eventDetailsFactory.createEventDetails(eventFilter, theLog));
                } else {
                    log.warn("Filter topic doesn't match  log!");
                }
            });
        });

        if (sub.isDisposed()) {
            //There was an error subscribing
            throw new BlockchainException(String.format(
                    "Failed to subcribe for filter %s.  The subscription is disposed.", eventFilter.getId()));
        }

        return new FilterSubscription(eventFilter, sub, startBlock);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public String getClientVersion() {
        try {
            final Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            return web3ClientVersion.getWeb3ClientVersion();
        } catch (IOException e) {
            throw new BlockchainException("Error when obtaining client version", e);
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public TransactionReceipt getTransactionReceipt(String txId) {
        try {
            final EthGetTransactionReceipt response = web3j.ethGetTransactionReceipt(txId).send();

            return response
                    .getTransactionReceipt()
                    .map(Web3jTransactionReceipt::new)
                    .orElse(null);
        } catch (IOException e) {
            throw new BlockchainException("Unable to connect to the ethereum client", e);
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public BigInteger getCurrentBlockNumber() {
        try {
            final EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();

            return ethBlockNumber.getBlockNumber();
        } catch (IOException e) {
            throw new BlockchainException("Error when obtaining the current block number", e);
        }
    }

    public Optional<Block> getBlock(String blockHash, boolean fullTransactionObjects) {
        try {
            final EthBlock blockResponse = web3j.ethGetBlockByHash(blockHash, fullTransactionObjects).send();

            if (blockResponse.getBlock() == null) {
                return Optional.empty();
            }

            return Optional.of(new Web3jBlock(blockResponse.getBlock(), nodeName));
        } catch (IOException e) {
            throw new BlockchainException("Error when obtaining block with hash: " + blockHash, e);
        }

    }

    public List<ContractEventDetails> getEventsForFilter(ContractEventFilter filter, BigInteger blockNumber) {

        try {
            final ContractEventSpecification eventSpec = filter.getEventSpecification();

            EthFilter ethFilter = new EthFilter(
                    new DefaultBlockParameterNumber(blockNumber),
                    new DefaultBlockParameterNumber(blockNumber), filter.getContractAddress());

            if (filter.getEventSpecification() != null) {
                ethFilter = ethFilter.addSingleTopic(Web3jUtil.getSignature(eventSpec));
            }

            final EthLog ethLog = web3j.ethGetLogs(ethFilter).send();

            return ethLog.getLogs()
                    .stream()
                    .map(log -> eventDetailsFactory.createEventDetails(filter, (Log) log))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new BlockchainException("Error when obtaining logs from block: " + blockNumber, e);
        }
    }

    @Override
    public String getRevertReason(String from, String to, BigInteger blockNumber, String input) {
        try {
            return web3j.ethCall(
                    Transaction.createEthCallTransaction(from, to, input),
                    DefaultBlockParameter.valueOf(blockNumber)
            ).send().getRevertReason();
        } catch (IOException e) {
            throw new BlockchainException("Error getting the revert reason", e);
        }
    }
}
