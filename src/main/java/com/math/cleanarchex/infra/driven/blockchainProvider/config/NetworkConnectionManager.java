package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.domain.entities.Network;
import com.math.cleanarchex.domain.entities.enums.ConnectionTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NetworkConnectionManager {

    private final Map<String, Web3j> networkConnections = new HashMap<>();

    public void connectToNetworks(List<Network> networks) {
        log.info("Connecting to networks: {}", networks);
        networkConnections.putAll(networks.stream()
                .collect(Collectors.toMap(Network::name, this::createWeb3jConnection)));
    }

    private Web3j createWeb3jConnection(Network network) {
        try {
            if (network.connectionType().equals(ConnectionTypeEnum.HTTP)) {
                return Web3j.build(new HttpService(network.endpoint()));
            } else {
                var web3jService = new WebSocketService(new WebSocketClient(new URI(network.endpoint())), false);
                web3jService.connect();
                return Web3j.build(web3jService);
            }
        } catch (Exception e) {
            log.error("Error connecting to network: {}", network.name(), e);
            return null;
        }
    }

    public Web3j getNetworkConnection(String networkName) {
        log.info("Getting connection for network: {}", networkName);
        return networkConnections.get(networkName);
    }

}
