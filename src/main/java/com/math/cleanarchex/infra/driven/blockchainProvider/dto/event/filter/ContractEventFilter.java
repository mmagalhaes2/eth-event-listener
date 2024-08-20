/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId.CorrelationIdStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;


@Document
@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractEventFilter {

    @Id
    private String id;

    private String contractAddress;

    private String node;

    private ContractEventSpecification eventSpecification;

    private CorrelationIdStrategy correlationIdStrategy;

    private BigInteger startBlock;
}
