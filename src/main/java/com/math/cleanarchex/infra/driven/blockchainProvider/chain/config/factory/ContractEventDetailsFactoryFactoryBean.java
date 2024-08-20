package com.math.cleanarchex.infra.driven.blockchainProvider.chain.config.factory;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.converter.EventParameterConverter;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.ContractEventDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.factory.DefaultContractEventDetailsFactory;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.Node;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.web3j.abi.datatypes.Type;

@Data
public class ContractEventDetailsFactoryFactoryBean
        implements FactoryBean<ContractEventDetailsFactory> {

    EventParameterConverter<Type> parameterConverter;
    Node node;
    String nodeName;

    @Override
    public ContractEventDetailsFactory getObject() throws Exception {
        return new DefaultContractEventDetailsFactory(
                parameterConverter, node, nodeName);
    }

    @Override
    public Class<?> getObjectType() {
        return ContractEventDetailsFactory.class;
    }
}
