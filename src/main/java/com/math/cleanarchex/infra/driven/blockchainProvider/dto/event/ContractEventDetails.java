package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.TransactionBasedDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.parameter.EventParameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Document
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractEventDetails implements TransactionBasedDetails {

    private String name;

    @Id
    private String filterId;

    private String nodeName;

    private List<EventParameter> indexedParameters;

    private List<EventParameter> nonIndexedParameters;

    private String transactionHash;

    private BigInteger logIndex;

    private BigInteger blockNumber;

    private String blockHash;

    private String address;

    private ContractEventStatus status = ContractEventStatus.UNCONFIRMED;

    private String eventSpecificationSignature;

    private String networkName;

    private BigInteger timestamp;

    public String getId() {
        return transactionHash + "-" + blockHash + "-" + logIndex;
    }
}
