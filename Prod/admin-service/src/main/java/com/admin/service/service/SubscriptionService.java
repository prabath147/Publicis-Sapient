package com.admin.service.service;

import com.admin.service.dto.SubscriptionsDto;
import com.admin.service.dto.PageableResponse;
import com.admin.service.entity.SubscriptionStatus;
import org.springframework.stereotype.Component;

@Component
public interface SubscriptionService {
    public PageableResponse<SubscriptionsDto> getAllSubscriptions(Integer pageNumber, Integer pageSize);
    public PageableResponse<SubscriptionsDto> getAllSubscriptionsByStatus(Integer pageNumber, Integer pageSize, SubscriptionStatus status);
    boolean checkIfExists(Long subscriptionId);
    public SubscriptionsDto getSubscriptionById(Long subscriptionId);
    SubscriptionsDto createSubscription(SubscriptionsDto subscriptionsDto) throws Exception;
    String deleteSubscription(Long subscriptionId);


}
