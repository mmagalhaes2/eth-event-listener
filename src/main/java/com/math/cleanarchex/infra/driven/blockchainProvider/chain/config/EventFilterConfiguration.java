package com.math.cleanarchex.infra.driven.blockchainProvider.chain.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.ContractEventFilter;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId.CorrelationIdStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId.IndexedParameterCorrelationIdStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter.correlationId.NonIndexedParameterCorrelationIdStrategy;
import com.math.cleanarchex.infra.driven.blockchainProvider.utils.ModelMapperFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Configuration
@ConfigurationProperties
@Data
public class EventFilterConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(EventFilterConfiguration.class);

    private List<EventFilterConfig> eventFilters;

    //Can't seem to be able to unmarshall from config to an interface using Spring.
    //This method converts CorrelationId to CorrelationId strategy
    public List<ContractEventFilter> getConfiguredEventFilters() {
        List<ContractEventFilter> filtersToReturn = new ArrayList<>();

        if (eventFilters != null) {
            final ModelMapper mapper = ModelMapperFactory.getInstance().createModelMapper();

            eventFilters.forEach((configFilter) -> {
                final ContractEventFilter contractEventFilter = new ContractEventFilter();
                mapper.map(configFilter, contractEventFilter);
                contractEventFilter.setContractAddress(Keys.toChecksumAddress(contractEventFilter.getContractAddress()));

                if (configFilter.getCorrelationId() != null) {
                    contractEventFilter.setCorrelationIdStrategy(configFilter.getCorrelationId().toStrategy());
                }
                contractEventFilter.setContractAddress(Keys.toChecksumAddress(contractEventFilter.getContractAddress()));
                filtersToReturn.add(contractEventFilter);
            });
        }

        return filtersToReturn;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class EventFilterConfig extends ContractEventFilter {
        private CorrelationId correlationId;
    }

    @Data
    public static class CorrelationId {
        private String type;

        private int index;

        private Map<String, Callable<CorrelationIdStrategy>> strategyCreatorMap = new HashMap<>();

        public CorrelationId() {
            strategyCreatorMap.put(IndexedParameterCorrelationIdStrategy.TYPE, () -> {
                return new IndexedParameterCorrelationIdStrategy(index);
            });

            strategyCreatorMap.put(NonIndexedParameterCorrelationIdStrategy.TYPE, () -> {
                return new NonIndexedParameterCorrelationIdStrategy(index);
            });
        }

        public CorrelationIdStrategy toStrategy() {
            try {
                return strategyCreatorMap.get(type).call();
            } catch (Exception e) {
                LOG.error("Error when obtaining correlation id strategy...application.yml probably incorrect", e);
                return null;
            }
        }
    }

}
