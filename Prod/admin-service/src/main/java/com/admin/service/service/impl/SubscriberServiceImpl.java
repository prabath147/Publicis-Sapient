package com.admin.service.service.impl;

import com.admin.service.dto.SubscriberDto;
import com.admin.service.entity.PaidStatus;
import com.admin.service.entity.Subscriber;
import com.admin.service.entity.Subscriptions;
import com.admin.service.entity.UserSubs;
import com.admin.service.exception.ResourceException;
import com.admin.service.repository.SubscriberRepository;
import com.admin.service.repository.SubscriptionsRepository;
import com.admin.service.repository.UserSubsRepository;
import com.admin.service.service.SubscriberService;
import com.admin.service.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

    private static final String RESOURCE_NAME = "Subscriber";
    private static final String FIELD_NAME_SUBSCRIBER = "subscriber";
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private SubscriptionsRepository subscriptionsRepository;
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserSubsRepository userSubsRepository;

    @Override
    public SubscriberDto getSubscriber(Long subscriberId) {
        Optional<Subscriber> subscriberDto = subscriberRepository.findById(subscriberId);
        if (subscriberDto.isEmpty()) {
            throw new ResourceException(RESOURCE_NAME, "ID", subscriberId, ResourceException.ErrorType.FOUND);
        }
        return modelMapper.map(subscriberDto.get(), SubscriberDto.class);
    }

    private Subscriber getIfExists(Long userId) {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(userId);
        if (subscriberOptional.isEmpty())
            throw new ResourceException("Subscriber", "ID", userId, ResourceException.ErrorType.FOUND);
        return subscriberOptional.get();
    }

    public boolean checkIfExistsSubscription(Subscriber subscriber, Long subscriptionId) {
        for (UserSubs userSubs : subscriber.getUserSubsSet())
            if (Objects.equals(userSubs.getSubscriptions().getSubscriptionId(), subscriptionId)) return true;
        return false;
    }


    @Override
    public SubscriberDto registerUserForSubscription(Long userId, Long subscriptionId) {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(userId);
        Subscriber subscriber;
        if (subscriberOptional.isEmpty()) {
            subscriber = new Subscriber(userId);
            try {
                subscriberRepository.save(subscriber);
            } catch (Exception e) {
                throw new ResourceException(RESOURCE_NAME, FIELD_NAME_SUBSCRIBER, subscriber, ResourceException.ErrorType.CREATED);
            }

        } else subscriber = subscriberOptional.get();
        if (checkIfExistsSubscription(subscriber, subscriptionId))
            throw new RuntimeException("You are already subscribed to this!");
        if (!subscriptionService.checkIfExists(subscriptionId))
            throw new RuntimeException("No such subscription exists!");
        Subscriptions subscriptions = modelMapper.map(subscriptionService.getSubscriptionById(subscriptionId), Subscriptions.class);
        UserSubs userSubs = new UserSubs();
        userSubs.setUserId(userId);
        userSubs.setStatus(PaidStatus.valueOf("PAID"));
        userSubs.setLastPaidDate(new Date());
        userSubs.setSubscriptions(subscriptions);
        userSubs.setSubEndDate(Date.from(LocalDate.now().plusDays(subscriptions.getPeriod()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Set<UserSubs> userSubsSet = subscriber.getUserSubsSet();
        userSubsSet.add(userSubs);
        subscriber.setUserSubsSet(userSubsSet);
        try {

            return modelMapper.map(subscriberRepository.save(subscriber), SubscriberDto.class);
        } catch (Exception e) {
            throw new ResourceException(RESOURCE_NAME, FIELD_NAME_SUBSCRIBER, subscriber, ResourceException.ErrorType.CREATED);
        }
    }



    @Override
    public void deleteSubscriber(Long subscriberId) {
        Subscriber subscriber = getIfExists(subscriberId);
        Set<UserSubs> userSubs = subscriber.getUserSubsSet();
        subscriber.setUserSubsSet(new HashSet<>());

        try {
            subscriberRepository.save(subscriber);
        } catch (Exception e) {
            throw new ResourceException(RESOURCE_NAME, "subscriber", subscriber, ResourceException.ErrorType.CREATED);
        }

        try {
            userSubsRepository.deleteAll(userSubs);
        } catch (Exception e) {
            throw new ResourceException("UserSubs", "user subs", userSubs, ResourceException.ErrorType.DELETED);
        }

        try {
            subscriberRepository.deleteById(subscriberId);
        } catch (Exception e) {
            throw new ResourceException(RESOURCE_NAME, "ID", subscriberId, ResourceException.ErrorType.DELETED);
        }
    }

    @Override
    public void removeSubscription(Long userId, Long subscriptionId) {
        Subscriber subscriber = getIfExists(userId);
        if (!checkIfExistsSubscription(subscriber, subscriptionId))
            throw new RuntimeException("You are not subscribed to this!");
        if (!subscriptionService.checkIfExists(subscriptionId))
            throw new RuntimeException("No such subscription exists!");
        Set<UserSubs> userSubsSet = subscriber.getUserSubsSet();
        UserSubs userSubs = null;
        for (UserSubs userSubs1 : userSubsSet)
            if (Objects.equals(userSubs1.getSubscriptions().getSubscriptionId(), subscriptionId)) {
                userSubs = userSubs1;
                break;
            }
        // TODO: user has not subscribed to this subscription
        if (userSubs == null)
            throw new ResourceException("Subscription", "ID", subscriptionId, ResourceException.ErrorType.FOUND);
        userSubsSet.remove(userSubs);
        subscriber.setUserSubsSet(userSubsSet);
        try {
            subscriberRepository.save(subscriber);
        } catch (Exception e) {
            throw new ResourceException(RESOURCE_NAME, FIELD_NAME_SUBSCRIBER, subscriber, ResourceException.ErrorType.CREATED);
        }

        try {
            userSubsRepository.delete(userSubs);
        } catch (Exception e) {
            throw new ResourceException("User Subscriptions", "expired Subscriptions", userSubs, ResourceException.ErrorType.DELETED);
        }
    }

    private List<Subscriptions> getSubscriptionByEndDate(Date date) {
        return subscriptionsRepository.findAllByEndDateBefore(date);
    }


    private List<Subscriber> getSubscribersFromUserSubs(List<UserSubs> userSubsList) {
        Map<Long, Set<UserSubs>> userSubsMap = new HashMap<>();
        Set<UserSubs> userSubsSet;
        List<Subscriber> subscribers = new ArrayList<>();

        for (UserSubs userSubs : userSubsList) {
            userSubsSet = userSubsMap.get(userSubs.getUserId());
            if (userSubsSet == null) userSubsSet = new HashSet<>();
            userSubsSet.add(userSubs);
            userSubsMap.put(userSubs.getUserId(), userSubsSet);
        }

        for (Map.Entry<Long, Set<UserSubs>> entry : userSubsMap.entrySet()) {
            Subscriber subscriber = getIfExists(entry.getKey());
            userSubsSet = subscriber.getUserSubsSet();
            for (UserSubs userSubs : entry.getValue())
            {
                userSubsSet.remove(userSubs);
                userSubsRepository.delete(userSubs);
            }
            subscriber.setUserSubsSet(userSubsSet);
            subscribers.add(subscriber);

        }

        return subscribers;
    }

    @Override
    @Scheduled(cron = "59 23 * * *")
    public void deleteExpiredSubscriptionForUser() throws ParseException {
        List<Subscriptions> expiredSubscriptionList = getSubscriptionByEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()));
        List<UserSubs> userSubsList = userSubsRepository.findAllBySubscriptionsIn(expiredSubscriptionList);
        List<Subscriber> subscribers = getSubscribersFromUserSubs(userSubsList);
        try {
            subscriberRepository.saveAll(subscribers);
        } catch (Exception e) {
            throw new ResourceException("Subscribers", "subscribers", subscribers, ResourceException.ErrorType.UPDATED);
        }
        try {
            subscriptionsRepository.deleteAll(expiredSubscriptionList);
        } catch (Exception e) {
            throw new ResourceException("Subscriptions", "expired Subscriptions", expiredSubscriptionList, ResourceException.ErrorType.DELETED);
        }
    }

    private List<UserSubs> getSubscriberByDate(Date date) {
        return userSubsRepository.findAllBySubEndDateBefore(date);
    }

    //for user
    @Override
    @Scheduled(cron = "59 23 * * *")
    public void deleteExpiredSubscriptionOnEndDate() throws ParseException {
        List<UserSubs> expiredUserSubs = getSubscriberByDate(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()));
        log.info("Expired UserSubs:-" + expiredUserSubs);
        List<Subscriber> subscribers = getSubscribersFromUserSubs(expiredUserSubs);
        log.info(String.valueOf(subscribers));
        try {
            subscriberRepository.saveAll(subscribers);
        } catch (Exception e) {
            throw new ResourceException("Subscribers", "subscribers", subscribers, ResourceException.ErrorType.UPDATED);
        }
        try {
            userSubsRepository.deleteAll(expiredUserSubs);
        } catch (Exception e) {
            throw new ResourceException("UserSubs", "user subs", expiredUserSubs, ResourceException.ErrorType.DELETED);
        }
    }
}
