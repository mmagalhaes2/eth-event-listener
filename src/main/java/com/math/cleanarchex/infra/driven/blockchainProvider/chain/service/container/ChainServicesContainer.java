package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container;

import java.util.List;

public interface ChainServicesContainer {

    NodeServices getNodeServices(String nodeName);

    List<String> getNodeNames();
}
