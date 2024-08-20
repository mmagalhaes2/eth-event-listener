package com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db;

import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.SaveableEventStore;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db.repository.ContractEventDetailsRepository;
import com.math.cleanarchex.infra.driven.blockchainProvider.integration.eventstore.db.repository.LatestBlockRepository;
import com.math.cleanarchex.infra.driven.blockchainProvider.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * A saveable event store that stores contract events in a db repository.
 */
public class MongoEventStore implements SaveableEventStore {

    private ContractEventDetailsRepository eventDetailsRepository;

    private LatestBlockRepository latestBlockRepository;

    private MongoTemplate mongoTemplate;

    public MongoEventStore(
            ContractEventDetailsRepository eventDetailsRepository,
            LatestBlockRepository latestBlockRepository,
            MongoTemplate mongoTemplate) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.latestBlockRepository = latestBlockRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination) {

        final Query query = new Query(
                Criteria.where("eventSpecificationSignature")
                        .is(eventSignature)
                        .and("address")
                        .is(contractAddress))
                .with(Sort.by(Direction.DESC, "blockNumber"))
                .collation(Collation.of("en").numericOrderingEnabled());

        final long totalResults = mongoTemplate.count(query, ContractEventDetails.class);

        //Set pagination on query
        query
                .skip(pagination.getPageNumber() * pagination.getPageSize())
                .limit(pagination.getPageSize());

        final List<ContractEventDetails> results = mongoTemplate.find(query, ContractEventDetails.class);

        return new PageImpl<>(results, pagination, totalResults);
    }

    @Override
    public Optional<LatestBlock> getLatestBlockForNode(String nodeName) {
        final Iterable<LatestBlock> blocks = latestBlockRepository.findAll();

        return latestBlockRepository.findById(nodeName);
    }

    @Override
    public boolean isPagingZeroIndexed() {
        return true;
    }

    @Override
    public void save(ContractEventDetails contractEventDetails) {
        eventDetailsRepository.save(contractEventDetails);
    }

    @Override
    public void save(LatestBlock latestBlock) {
        latestBlockRepository.save(latestBlock);
    }
}
