package com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.wrapper;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.Log;
import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.domain.TransactionReceipt;
import com.math.cleanarchex.infra.driven.blockchainProvider.utils.ModelMapperFactory;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Web3jTransactionReceipt implements TransactionReceipt {

    private String transactionHash;
    private BigInteger transactionIndex;
    private String blockHash;
    private BigInteger blockNumber;
    private BigInteger cumulativeGasUsed;
    private BigInteger gasUsed;
    private String contractAddress;
    private String root;
    private String from;
    private String to;
    private List<Log> logs;
    private String logsBloom;
    private String status;

    public Web3jTransactionReceipt(
            org.web3j.protocol.core.methods.response.TransactionReceipt web3TransactionReceipt) {

        logs = convertLogs(web3TransactionReceipt.getLogs());

        try {
            final ModelMapper modelMapper = ModelMapperFactory.getInstance().createModelMapper();
            //Skip logs
            modelMapper.getConfiguration().setPropertyCondition(ctx ->
                    !ctx.getMapping().getLastDestinationProperty().getName().equals("logs"));
            modelMapper.map(web3TransactionReceipt, this);
        } catch (RuntimeException re) {
            re.printStackTrace();
            throw re;
        }
    }

    private List<Log> convertLogs(List<org.web3j.protocol.core.methods.response.Log> logs) {
        return logs.stream()
                .map(log -> new Web3jLog(log))
                .collect(Collectors.toList());
    }
}
