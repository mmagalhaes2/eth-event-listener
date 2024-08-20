package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.container;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Lazy
@Component
public class DefaultBlockchainServiceContainer implements ChainServicesContainer, InitializingBean {

    private List<NodeServices> nodeServices;
    private Map<String, NodeServices> nodeServicesMap;

    @Autowired
    public DefaultBlockchainServiceContainer(@Lazy List<NodeServices> nodeServices) {
        this.nodeServices = nodeServices;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        nodeServicesMap = new HashMap<>();
        nodeServices.forEach(ns -> nodeServicesMap.put(ns.getNodeName(), ns));
    }

    @Override
    public NodeServices getNodeServices(String nodeName) {
        return nodeServicesMap.get(nodeName);
    }

    @Override
    public List<String> getNodeNames() {
        return new ArrayList(nodeServicesMap.keySet());
    }
}
