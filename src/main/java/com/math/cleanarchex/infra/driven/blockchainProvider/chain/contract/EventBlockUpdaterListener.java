package com.math.cleanarchex.infra.driven.blockchainProvider.chain.contract;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.service.block.EventBlockManagementService;
import com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.ContractEventDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventBlockUpdaterListener implements ContractEventListener {

    private EventBlockManagementService blockManagement;

    @Autowired
    public EventBlockUpdaterListener(EventBlockManagementService blockManagement) {
        this.blockManagement = blockManagement;
    }

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        blockManagement.updateLatestBlock(eventDetails.getEventSpecificationSignature(), eventDetails.getBlockNumber(), eventDetails.getAddress());
    }
}
