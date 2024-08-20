package com.math.cleanarchex.infra.driven.blockchainProvider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.math.BigInteger;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFilterSyncStatus {

    @Id
    private String filterId;

    private BigInteger lastBlockNumber;

    private SyncStatus syncStatus;

}
