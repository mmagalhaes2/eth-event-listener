package com.math.cleanarchex.infra.driven.blockchainProvider.chain.config.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.ContractEventDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.BlockchainService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.Web3jService;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.Node;
import com.math.cleanarchex.infra.driven.blockchainProvider.service.AsyncTaskService;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.web3j.protocol.Web3j;

@Data
public class BlockchainServiceFactoryBean implements FactoryBean<BlockchainService> {

    private Node node;
    private Web3j web3j;
    private ContractEventDetailsFactory contractEventDetailsFactory;
    private AsyncTaskService asyncTaskService;

    @Override
    public BlockchainService getObject() throws Exception {
        return new Web3jService(node.getName(), web3j, contractEventDetailsFactory, asyncTaskService);
    }

    @Override
    public Class<?> getObjectType() {
        return BlockchainService.class;
    }
}
