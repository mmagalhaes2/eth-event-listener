package com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.TransactionBasedDetails;
import lombok.*;

import java.math.BigInteger;

@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetails implements TransactionBasedDetails {

    private String hash;
    private String nonce;
    private String blockHash;
    private String blockNumber;
    private String transactionIndex;
    private String from;
    private String to;
    private String value;
    private String nodeName;
    private String contractAddress;
    private String input;
    private String revertReason;
    private BigInteger timestamp;

    private TransactionStatus status;

    @JsonIgnore
    public String getTransactionHash() {
        return hash;
    }
}
