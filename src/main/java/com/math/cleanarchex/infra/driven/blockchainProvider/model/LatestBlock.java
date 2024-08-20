package com.math.cleanarchex.infra.driven.blockchainProvider.model;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.block.BlockDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document

@Data
@NoArgsConstructor
public class LatestBlock {

    @Id
    private String nodeName;
    private BigInteger number;
    private String hash;
    private BigInteger timestamp;

    public LatestBlock(BlockDetails blockDetails) {
        this.nodeName = blockDetails.getNodeName();
        this.number = blockDetails.getNumber();
        this.hash = blockDetails.getHash();
        this.timestamp = blockDetails.getTimestamp();
    }
}
