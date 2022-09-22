package com.admin.service.service;

import com.admin.service.dto.SubscriberDto;
import com.admin.service.dto.SubscriptionsDto;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public interface SubscriberService {
    SubscriberDto getSubscriber(Long subscriberId);
    SubscriberDto registerUserForSubscription(Long userId, Long subscriptionId);

    void deleteSubscriber(Long userId);
    void removeSubscription(Long userId, Long subscriptionId);
    void deleteExpiredSubscriptionOnEndDate() throws ParseException;
    void deleteExpiredSubscriptionForUser() throws ParseException;
}
