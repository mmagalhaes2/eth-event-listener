package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.math.cleanarchex.domain.entities.EventSpecification;
import com.math.cleanarchex.domain.entities.Monitor;
import com.math.cleanarchex.domain.entities.Parameter;
import com.math.cleanarchex.infra.driven.blockchainProvider.config.NetworkConnectionManager;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkService {

    private final NetworkConnectionManager networkConnectionManager;

    public void performNetworkOperations(Monitor monitor) {
        Web3j web3j = networkConnectionManager.getNetworkConnection(monitor.nodeIdentifier());

        Event eventTopic = createEvent(monitor.eventSpecification());

        EthFilter filter = buildEthFilter(monitor, eventTopic);

        Flowable<Log> logFlowable = web3j.ethLogFlowable(filter);

        subscribeToFlowable(logFlowable);
    }

    private Event createEvent(EventSpecification eventSpecification) {
        List<TypeReference<?>> parameters = new ArrayList<>();

        Optional.ofNullable(eventSpecification.parameters())
                .ifPresent(list -> {
                    List<Parameter> indexedParameters = sortParametersByPosition(eventSpecification.parameters());

//                    indexedParameters.forEach(parameter -> parameters.add(getTypeReference(parameter, Boolean.TRUE)));
                });

        return new Event(eventSpecification.name(), parameters);
    }

    private EthFilter buildEthFilter(Monitor monitor, Event eventTopic) {
        return new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, monitor.eventSpecification().contractAddress()).addSingleTopic(EventEncoder.encode(eventTopic));
    }

    private void subscribeToFlowable(Flowable<Log> logFlowable) {
        logFlowable.subscribe(response -> {
            log.info("Log: {}", response);
        }, Throwable::printStackTrace, () -> log.info("Flowable completed")).isDisposed();
    }

    private List<Parameter> sortParametersByPosition(List<Parameter> parameters) {
        return parameters.stream()
                .sorted(Comparator.comparing(Parameter::position))
                .collect(Collectors.toList());
    }

}
