package com.math.cleanarchex.infra.driven.blockchainProvider.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionIdentifier {

    String hash;

    String nodeName;
}
