package com.math.cleanarchex.infra.driven.blockchainProvider.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction.TransactionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document
@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class TransactionMonitoringSpec {

    @Id
    private String id;

    private TransactionIdentifierType type;

    private String nodeName;

    private List<TransactionStatus> statuses = new ArrayList(
            Arrays.asList(TransactionStatus.UNCONFIRMED, TransactionStatus.CONFIRMED, TransactionStatus.FAILED));

    private String transactionIdentifierValue;

    public TransactionMonitoringSpec(TransactionIdentifierType type,
                                     String transactionIdentifierValue,
                                     String nodeName,
                                     List<TransactionStatus> statuses) {
        this.type = type;
        this.transactionIdentifierValue = transactionIdentifierValue;
        this.nodeName = nodeName;

        if (statuses != null && !statuses.isEmpty()) {
            this.statuses = statuses;
        }

        convertToCheckSum();

        this.id = Hash.sha3String(transactionIdentifierValue + type + nodeName + this.statuses.toString()).substring(2);
    }

    public TransactionMonitoringSpec(TransactionIdentifierType type,
                                     String transactionIdentifierValue,
                                     String nodeName) {
        this(type, transactionIdentifierValue, nodeName, null);
    }

    @JsonSetter("type")
    public void setType(String type) {
        this.type = TransactionIdentifierType.valueOf(type.toUpperCase());
    }

    @JsonSetter("type")
    public void setType(TransactionIdentifierType type) {
        this.type = type;
    }

    public void generateId() {
        this.id = Hash.sha3String(transactionIdentifierValue + type + nodeName + statuses.toString()).substring(2);
    }

    public void convertToCheckSum() {
        if (this.type != TransactionIdentifierType.HASH) {
            this.transactionIdentifierValue = Keys.toChecksumAddress(this.transactionIdentifierValue);
        }
    }
}
