package com.math.cleanarchex.domain.entities;

import com.math.cleanarchex.domain.entities.enums.NotificationStatusEnum;

import java.util.List;

public record TransactionSpecification(String type,
                                       String identifier,
                                       List<NotificationStatusEnum> notificationStatuses,
                                       Boolean blockCreatedIndicator,
                                       Boolean failedReasonIndicator) {
}
