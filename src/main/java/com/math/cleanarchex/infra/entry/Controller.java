package com.math.cleanarchex.infra.entry;

import com.math.cleanarchex.domain.entities.Monitor;
import com.math.cleanarchex.domain.entities.Network;
import com.math.cleanarchex.infra.driven.blockchainProvider.config.NetworkConnectionManager;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.NetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final NetworkService networkService;

    private final NetworkConnectionManager networkConnectionManager;

    @PostMapping("/newConnection")
    public String manageState(@RequestBody List<Network> endpoint) {
        log.info("State managed for endpoint: {}", endpoint);
        networkConnectionManager.connectToNetworks(endpoint);
        return "State managed for endpoint: " + endpoint;
    }

    @PostMapping("/testConnection")
    public String performNetworkOperations(@RequestBody Monitor monitor) {
        log.info("Performing network operations for network: {}", monitor);
        networkService.performNetworkOperations(monitor);
        return "Network operations performed for network: " + monitor;
    }
}
