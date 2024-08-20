package com.math.cleanarchex.infra.driven.blockchainProvider.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BlockEvent.class, name = BlockEvent.TYPE),
        @JsonSubTypes.Type(value = ContractEvent.class, name = ContractEvent.TYPE),
        @JsonSubTypes.Type(value = TransactionEvent.class, name = TransactionEvent.TYPE),
        @JsonSubTypes.Type(value = ContractEventFilterAdded.class, name = ContractEventFilterAdded.TYPE),
        @JsonSubTypes.Type(value = ContractEventFilterRemoved.class, name = ContractEventFilterRemoved.TYPE),
        @JsonSubTypes.Type(value = TransactionMonitorAdded.class, name = TransactionMonitorAdded.TYPE),
        @JsonSubTypes.Type(value = TransactionMonitorRemoved.class, name = TransactionMonitorRemoved.TYPE)
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventeumMessage<T> {
    String getId();

    String getType();

    T getDetails();
}
