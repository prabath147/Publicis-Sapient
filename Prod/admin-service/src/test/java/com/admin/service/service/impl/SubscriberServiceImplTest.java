package com.admin.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.admin.service.dto.SubscriberDto;
import com.admin.service.dto.SubscriptionsDto;
import com.admin.service.entity.Benefits;
import com.admin.service.entity.PaidStatus;
import com.admin.service.entity.Subscriber;
import com.admin.service.entity.SubscriptionStatus;
import com.admin.service.entity.Subscriptions;
import com.admin.service.entity.UserSubs;
import com.admin.service.exception.ResourceException;
import com.admin.service.repository.SubscriberRepository;
import com.admin.service.repository.SubscriptionsRepository;
import com.admin.service.repository.UserSubsRepository;
import com.admin.service.service.SubscriptionService;

import java.text.ParseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

@ContextConfiguration(classes = {SubscriberServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SubscriberServiceImplTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberServiceImpl subscriberServiceImpl;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private SubscriptionsRepository subscriptionsRepository;

    @MockBean
    private UserSubsRepository userSubsRepository;


    @Test
    void testGetSubscriber() {
        SubscriberDto subscriberDto = new SubscriberDto();
        when(modelMapper.map((Object) any(), (Class<SubscriberDto>) any())).thenReturn(subscriberDto);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(subscriberDto, subscriberServiceImpl.getSubscriber(123L));
        verify(modelMapper).map((Object) any(), (Class<SubscriberDto>) any());
        verify(subscriberRepository).findById((Long) any());
    }


    @Test
    void testGetSubscriber2() {
        when(modelMapper.map((Object) any(), (Class<SubscriberDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.getSubscriber(123L));
        verify(modelMapper).map((Object) any(), (Class<SubscriberDto>) any());
        verify(subscriberRepository).findById((Long) any());
    }

    @Test
    void testGetSubscriber3() {
        when(subscriberRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.getSubscriber(123L));
    }


    @Test
    void testCheckIfExistsSubscription() {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        assertFalse(subscriberServiceImpl.checkIfExistsSubscription(subscriber, 123L));
    }


    @Test
    void testCheckIfExistsSubscription2() {
        Subscriber subscriber = mock(Subscriber.class);
        when(subscriber.getUserSubsSet()).thenReturn(new HashSet<>());
        doNothing().when(subscriber).setUserId((Long) any());
        doNothing().when(subscriber).setUserSubsSet((Set<UserSubs>) any());
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        assertFalse(subscriberServiceImpl.checkIfExistsSubscription(subscriber, 123L));
        verify(subscriber).getUserSubsSet();
        verify(subscriber).setUserId((Long) any());
        verify(subscriber).setUserSubsSet((Set<UserSubs>) any());
    }


    @Test
    void testCheckIfExistsSubscription3() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("Subscription Name");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);
        Subscriber subscriber = mock(Subscriber.class);
        when(subscriber.getUserSubsSet()).thenReturn(userSubsSet);
        doNothing().when(subscriber).setUserId((Long) any());
        doNothing().when(subscriber).setUserSubsSet((Set<UserSubs>) any());
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        assertTrue(subscriberServiceImpl.checkIfExistsSubscription(subscriber, 123L));
        verify(subscriber).getUserSubsSet();
        verify(subscriber).setUserId((Long) any());
        verify(subscriber).setUserSubsSet((Set<UserSubs>) any());
    }


    @Test
    void testCheckIfExistsSubscription4() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("Subscription Name");

        Subscriptions subscriptions1 = new Subscriptions();
        subscriptions1.setBenefits(new Benefits(10.0d, true));
        subscriptions1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setEndDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setPeriod(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setStartDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions1.setSubscriptionCost(10.0d);
        subscriptions1.setSubscriptionId(123L);
        subscriptions1.setSubscriptionName("Subscription Name");
        UserSubs userSubs = mock(UserSubs.class);
        when(userSubs.getSubscriptions()).thenReturn(subscriptions1);
        doNothing().when(userSubs).setLastPaidDate((Date) any());
        doNothing().when(userSubs).setStatus((PaidStatus) any());
        doNothing().when(userSubs).setSubEndDate((Date) any());
        doNothing().when(userSubs).setSubscriptions((Subscriptions) any());
        doNothing().when(userSubs).setUserId((Long) any());
        doNothing().when(userSubs).setUserSubId((Long) any());
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);
        Subscriber subscriber = mock(Subscriber.class);
        when(subscriber.getUserSubsSet()).thenReturn(userSubsSet);
        doNothing().when(subscriber).setUserId((Long) any());
        doNothing().when(subscriber).setUserSubsSet((Set<UserSubs>) any());
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        assertTrue(subscriberServiceImpl.checkIfExistsSubscription(subscriber, 123L));
        verify(subscriber).getUserSubsSet();
        verify(subscriber).setUserId((Long) any());
        verify(subscriber).setUserSubsSet((Set<UserSubs>) any());
        verify(userSubs).getSubscriptions();
        verify(userSubs).setLastPaidDate((Date) any());
        verify(userSubs).setStatus((PaidStatus) any());
        verify(userSubs).setSubEndDate((Date) any());
        verify(userSubs).setSubscriptions((Subscriptions) any());
        verify(userSubs).setUserId((Long) any());
        verify(userSubs).setUserSubId((Long) any());
    }


    @Test
    void testCheckIfExistsSubscription5() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("Subscription Name");
        Subscriptions subscriptions1 = mock(Subscriptions.class);
        when(subscriptions1.getSubscriptionId()).thenReturn(1L);
        doNothing().when(subscriptions1).setBenefits((Benefits) any());
        doNothing().when(subscriptions1).setDescription((String) any());
        doNothing().when(subscriptions1).setEndDate((Date) any());
        doNothing().when(subscriptions1).setPeriod(anyInt());
        doNothing().when(subscriptions1).setStartDate((Date) any());
        doNothing().when(subscriptions1).setStatus((SubscriptionStatus) any());
        doNothing().when(subscriptions1).setSubscriptionCost((Double) any());
        doNothing().when(subscriptions1).setSubscriptionId((Long) any());
        doNothing().when(subscriptions1).setSubscriptionName((String) any());
        subscriptions1.setBenefits(new Benefits(10.0d, true));
        subscriptions1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setEndDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setPeriod(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setStartDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions1.setSubscriptionCost(10.0d);
        subscriptions1.setSubscriptionId(123L);
        subscriptions1.setSubscriptionName("Subscription Name");
        UserSubs userSubs = mock(UserSubs.class);
        when(userSubs.getSubscriptions()).thenReturn(subscriptions1);
        doNothing().when(userSubs).setLastPaidDate((Date) any());
        doNothing().when(userSubs).setStatus((PaidStatus) any());
        doNothing().when(userSubs).setSubEndDate((Date) any());
        doNothing().when(userSubs).setSubscriptions((Subscriptions) any());
        doNothing().when(userSubs).setUserId((Long) any());
        doNothing().when(userSubs).setUserSubId((Long) any());
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);
        Subscriber subscriber = mock(Subscriber.class);
        when(subscriber.getUserSubsSet()).thenReturn(userSubsSet);
        doNothing().when(subscriber).setUserId((Long) any());
        doNothing().when(subscriber).setUserSubsSet((Set<UserSubs>) any());
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        assertFalse(subscriberServiceImpl.checkIfExistsSubscription(subscriber, 123L));
        verify(subscriber).getUserSubsSet();
        verify(subscriber).setUserId((Long) any());
        verify(subscriber).setUserSubsSet((Set<UserSubs>) any());
        verify(userSubs).getSubscriptions();
        verify(userSubs).setLastPaidDate((Date) any());
        verify(userSubs).setStatus((PaidStatus) any());
        verify(userSubs).setSubEndDate((Date) any());
        verify(userSubs).setSubscriptions((Subscriptions) any());
        verify(userSubs).setUserId((Long) any());
        verify(userSubs).setUserSubId((Long) any());
        verify(subscriptions1).getSubscriptionId();
        verify(subscriptions1).setBenefits((Benefits) any());
        verify(subscriptions1).setDescription((String) any());
        verify(subscriptions1).setEndDate((Date) any());
        verify(subscriptions1).setPeriod(anyInt());
        verify(subscriptions1).setStartDate((Date) any());
        verify(subscriptions1).setStatus((SubscriptionStatus) any());
        verify(subscriptions1).setSubscriptionCost((Double) any());
        verify(subscriptions1).setSubscriptionId((Long) any());
        verify(subscriptions1).setSubscriptionName((String) any());
    }


    @Test
    void testRegisterUserForSubscription() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("Subscription Name");
        when(modelMapper.map((Object) any(), (Class<Subscriptions>) any())).thenReturn(subscriptions);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber1);
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriptionService.getSubscriptionById((Long) any())).thenReturn(new SubscriptionsDto());
        when(subscriptionService.checkIfExists((Long) any())).thenReturn(true);
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.registerUserForSubscription(123L, 123L));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriptionService).checkIfExists((Long) any());
        verify(subscriptionService).getSubscriptionById((Long) any());
    }

    @Test
    void testRegisterUserForSubscription1() {
        when(subscriberRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(subscriberRepository.save(any())).thenThrow(new EntityNotFoundException());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.registerUserForSubscription(123L,1L));
    }
//    @Test
//    void testRegisterUserForSubscription2() {
//        when(subscriberRepository.findById(anyLong())).thenReturn(Optional.empty());
//        when(subscriberRepository.save(any())).thenReturn(new Subscriber());
//        when(subscriberServiceImpl.checkIfExistsSubscription(any(),anyLong())).thenReturn(true);
//        assertThrows(RuntimeException.class, () -> subscriberServiceImpl.registerUserForSubscription(123L,1L));
//    }

    @Test
    void testDeleteSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        doNothing().when(subscriberRepository).deleteById((Long) any());
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        doNothing().when(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
        subscriberServiceImpl.deleteSubscriber(123L);
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriberRepository).deleteById((Long) any());
        verify(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
    }


    @Test
    void testDeleteSubscriber2() {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        doNothing().when(subscriberRepository).deleteById((Long) any());
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(userSubsRepository)
                .deleteAll((Iterable<UserSubs>) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteSubscriber(123L));
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
    }


    @Test
    void testDeleteSubscriber3() {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        doNothing().when(subscriberRepository).deleteById((Long) any());
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        doThrow(new RuntimeException("An error occurred")).when(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteSubscriber(123L));
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
    }


    @Test
    void testDeleteSubscriber4() {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber);
        doNothing().when(subscriberRepository).deleteById((Long) any());
        when(subscriberRepository.findById((Long) any())).thenReturn(Optional.empty());
        doNothing().when(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteSubscriber(123L));
        verify(subscriberRepository).findById((Long) any());
    }


    @Test
    void testDeleteSubscriber5() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("UserSubs");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(userSubsSet);
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        doNothing().when(subscriberRepository).deleteById((Long) any());
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(userSubsRepository)
                .deleteAll((Iterable<UserSubs>) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteSubscriber(123L));
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
    }


    @Test
    void testRemoveSubscription() {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(RuntimeException.class, () -> subscriberServiceImpl.removeSubscription(123L, 123L));
        verify(subscriberRepository).findById((Long) any());
    }


    @Test
    void testRemoveSubscription2() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("You are not subscribed to this!");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(userSubsSet);
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriptionService.checkIfExists((Long) any())).thenReturn(true);
        doNothing().when(userSubsRepository).delete((UserSubs) any());
        subscriberServiceImpl.removeSubscription(123L, 123L);
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriptionService).checkIfExists((Long) any());
        verify(userSubsRepository).delete((UserSubs) any());
    }


    @Test
    void testRemoveSubscription3() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("You are not subscribed to this!");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(userSubsSet);
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriptionService.checkIfExists((Long) any())).thenReturn(true);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(userSubsRepository)
                .delete((UserSubs) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.removeSubscription(123L, 123L));
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriptionService).checkIfExists((Long) any());
        verify(userSubsRepository).delete((UserSubs) any());
    }


    @Test
    void testRemoveSubscription4() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("You are not subscribed to this!");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(userSubsSet);
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriptionService.checkIfExists((Long) any())).thenReturn(true);
        doThrow(new RuntimeException("An error occurred")).when(userSubsRepository).delete((UserSubs) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.removeSubscription(123L, 123L));
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriptionService).checkIfExists((Long) any());
        verify(userSubsRepository).delete((UserSubs) any());
    }


    @Test
    void testRemoveSubscription5() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("You are not subscribed to this!");

        Subscriptions subscriptions1 = new Subscriptions();
        subscriptions1.setBenefits(new Benefits(10.0d, true));
        subscriptions1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setEndDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setPeriod(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setStartDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions1.setSubscriptionCost(10.0d);
        subscriptions1.setSubscriptionId(123L);
        subscriptions1.setSubscriptionName("Subscription Name");
        UserSubs userSubs = mock(UserSubs.class);
        when(userSubs.getSubscriptions()).thenReturn(subscriptions1);
        doNothing().when(userSubs).setLastPaidDate((Date) any());
        doNothing().when(userSubs).setStatus((PaidStatus) any());
        doNothing().when(userSubs).setSubEndDate((Date) any());
        doNothing().when(userSubs).setSubscriptions((Subscriptions) any());
        doNothing().when(userSubs).setUserId((Long) any());
        doNothing().when(userSubs).setUserSubId((Long) any());
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(userSubsSet);
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriptionService.checkIfExists((Long) any())).thenReturn(true);
        doNothing().when(userSubsRepository).delete((UserSubs) any());
        subscriberServiceImpl.removeSubscription(123L, 123L);
        verify(subscriberRepository).save((Subscriber) any());
        verify(subscriberRepository).findById((Long) any());
        verify(userSubs, atLeast(1)).getSubscriptions();
        verify(userSubs).setLastPaidDate((Date) any());
        verify(userSubs).setStatus((PaidStatus) any());
        verify(userSubs).setSubEndDate((Date) any());
        verify(userSubs).setSubscriptions((Subscriptions) any());
        verify(userSubs).setUserId((Long) any());
        verify(userSubs).setUserSubId((Long) any());
        verify(subscriptionService).checkIfExists((Long) any());
        verify(userSubsRepository).delete((UserSubs) any());
    }


    @Test
    void testRemoveSubscription6() {
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("You are not subscribed to this!");
        Subscriptions subscriptions1 = mock(Subscriptions.class);
        when(subscriptions1.getSubscriptionId()).thenReturn(1L);
        doNothing().when(subscriptions1).setBenefits((Benefits) any());
        doNothing().when(subscriptions1).setDescription((String) any());
        doNothing().when(subscriptions1).setEndDate((Date) any());
        doNothing().when(subscriptions1).setPeriod(anyInt());
        doNothing().when(subscriptions1).setStartDate((Date) any());
        doNothing().when(subscriptions1).setStatus((SubscriptionStatus) any());
        doNothing().when(subscriptions1).setSubscriptionCost((Double) any());
        doNothing().when(subscriptions1).setSubscriptionId((Long) any());
        doNothing().when(subscriptions1).setSubscriptionName((String) any());
        subscriptions1.setBenefits(new Benefits(10.0d, true));
        subscriptions1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setEndDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setPeriod(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions1.setStartDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions1.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions1.setSubscriptionCost(10.0d);
        subscriptions1.setSubscriptionId(123L);
        subscriptions1.setSubscriptionName("Subscription Name");
        UserSubs userSubs = mock(UserSubs.class);
        when(userSubs.getSubscriptions()).thenReturn(subscriptions1);
        doNothing().when(userSubs).setLastPaidDate((Date) any());
        doNothing().when(userSubs).setStatus((PaidStatus) any());
        doNothing().when(userSubs).setSubEndDate((Date) any());
        doNothing().when(userSubs).setSubscriptions((Subscriptions) any());
        doNothing().when(userSubs).setUserId((Long) any());
        doNothing().when(userSubs).setUserSubId((Long) any());
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        HashSet<UserSubs> userSubsSet = new HashSet<>();
        userSubsSet.add(userSubs);

        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(userSubsSet);
        Optional<Subscriber> ofResult = Optional.of(subscriber);

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setUserId(123L);
        subscriber1.setUserSubsSet(new HashSet<>());
        when(subscriberRepository.save((Subscriber) any())).thenReturn(subscriber1);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriptionService.checkIfExists((Long) any())).thenReturn(true);
        doNothing().when(userSubsRepository).delete((UserSubs) any());
        assertThrows(RuntimeException.class, () -> subscriberServiceImpl.removeSubscription(123L, 123L));
        verify(subscriberRepository).findById((Long) any());
        verify(userSubs).getSubscriptions();
        verify(userSubs).setLastPaidDate((Date) any());
        verify(userSubs).setStatus((PaidStatus) any());
        verify(userSubs).setSubEndDate((Date) any());
        verify(userSubs).setSubscriptions((Subscriptions) any());
        verify(userSubs).setUserId((Long) any());
        verify(userSubs).setUserSubId((Long) any());
        verify(subscriptions1).getSubscriptionId();
        verify(subscriptions1).setBenefits((Benefits) any());
        verify(subscriptions1).setDescription((String) any());
        verify(subscriptions1).setEndDate((Date) any());
        verify(subscriptions1).setPeriod(anyInt());
        verify(subscriptions1).setStartDate((Date) any());
        verify(subscriptions1).setStatus((SubscriptionStatus) any());
        verify(subscriptions1).setSubscriptionCost((Double) any());
        verify(subscriptions1).setSubscriptionId((Long) any());
        verify(subscriptions1).setSubscriptionName((String) any());
    }




    @Test
    void testDeleteExpiredSubscriptionForUser2() throws ParseException {
        when(subscriberRepository.saveAll((Iterable<Subscriber>) any())).thenReturn(new ArrayList<>());
        when(subscriptionsRepository.findAllByEndDateBefore((Date) any())).thenReturn(new ArrayList<>());
        doNothing().when(subscriptionsRepository).deleteAll((Iterable<Subscriptions>) any());
        when(userSubsRepository.findAllBySubscriptionsIn((List<Subscriptions>) any())).thenThrow(
                new ResourceException("yyyy-MM-dd", "yyyy-MM-dd", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteExpiredSubscriptionForUser());
        verify(subscriptionsRepository).findAllByEndDateBefore((Date) any());
        verify(userSubsRepository).findAllBySubscriptionsIn((List<Subscriptions>) any());
    }


    @Test
    void testDeleteExpiredSubscriptionForUser4() throws ParseException {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriberRepository.saveAll((Iterable<Subscriber>) any())).thenReturn(new ArrayList<>());
        when(subscriptionsRepository.findAllByEndDateBefore((Date) any())).thenReturn(new ArrayList<>());
        doNothing().when(subscriptionsRepository).deleteAll((Iterable<Subscriptions>) any());

        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("yyyy-MM-dd");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        ArrayList<UserSubs> userSubsList = new ArrayList<>();
        userSubsList.add(userSubs);
        doThrow(new ResourceException("yyyy-MM-dd", "yyyy-MM-dd", "Field Value", ResourceException.ErrorType.CREATED))
                .when(userSubsRepository)
                .delete((UserSubs) any());
        when(userSubsRepository.findAllBySubscriptionsIn((List<Subscriptions>) any())).thenReturn(userSubsList);
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteExpiredSubscriptionForUser());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriptionsRepository).findAllByEndDateBefore((Date) any());
        verify(userSubsRepository).findAllBySubscriptionsIn((List<Subscriptions>) any());
        verify(userSubsRepository).delete((UserSubs) any());
    }


    @Test
    void testDeleteExpiredSubscriptionForUser7() throws ParseException {
        when(subscriberRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(subscriberRepository.saveAll((Iterable<Subscriber>) any())).thenReturn(new ArrayList<>());
        when(subscriptionsRepository.findAllByEndDateBefore((Date) any())).thenReturn(new ArrayList<>());
        doNothing().when(subscriptionsRepository).deleteAll((Iterable<Subscriptions>) any());

        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("yyyy-MM-dd");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        ArrayList<UserSubs> userSubsList = new ArrayList<>();
        userSubsList.add(userSubs);
        doNothing().when(userSubsRepository).delete((UserSubs) any());
        when(userSubsRepository.findAllBySubscriptionsIn((List<Subscriptions>) any())).thenReturn(userSubsList);
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteExpiredSubscriptionForUser());
        verify(subscriberRepository).findById((Long) any());
        verify(subscriptionsRepository).findAllByEndDateBefore((Date) any());
        verify(userSubsRepository).findAllBySubscriptionsIn((List<Subscriptions>) any());
    }


    @Test
    void testDeleteExpiredSubscriptionOnEndDate2() throws ParseException {
        when(subscriberRepository.saveAll((Iterable<Subscriber>) any())).thenReturn(new ArrayList<>());
        when(userSubsRepository.findAllBySubEndDateBefore((Date) any()))
                .thenThrow(new RuntimeException("An error occurred"));
        doThrow(new RuntimeException("An error occurred")).when(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
        assertThrows(RuntimeException.class, () -> subscriberServiceImpl.deleteExpiredSubscriptionOnEndDate());
        verify(userSubsRepository).findAllBySubEndDateBefore((Date) any());
    }


    @Test
    void testDeleteExpiredSubscriptionOnEndDate4() throws ParseException {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(123L);
        subscriber.setUserSubsSet(new HashSet<>());
        Optional<Subscriber> ofResult = Optional.of(subscriber);
        when(subscriberRepository.findById((Long) any())).thenReturn(ofResult);
        when(subscriberRepository.saveAll((Iterable<Subscriber>) any())).thenReturn(new ArrayList<>());

        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("yyyy-MM-dd");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        ArrayList<UserSubs> userSubsList = new ArrayList<>();
        userSubsList.add(userSubs);
        doThrow(new ResourceException("yyyy-MM-dd", "yyyy-MM-dd", "Field Value", ResourceException.ErrorType.CREATED))
                .when(userSubsRepository)
                .delete((UserSubs) any());
        when(userSubsRepository.findAllBySubEndDateBefore((Date) any())).thenReturn(userSubsList);
        doNothing().when(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteExpiredSubscriptionOnEndDate());
        verify(subscriberRepository).findById((Long) any());
        verify(userSubsRepository).findAllBySubEndDateBefore((Date) any());
        verify(userSubsRepository).delete((UserSubs) any());
    }



    @Test
    void testDeleteExpiredSubscriptionOnEndDate7() throws ParseException {
        when(subscriberRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(subscriberRepository.saveAll((Iterable<Subscriber>) any())).thenReturn(new ArrayList<>());

        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setBenefits(new Benefits(10.0d, true));
        subscriptions.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setPeriod(1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        subscriptions.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        subscriptions.setStatus(SubscriptionStatus.ACTIVE);
        subscriptions.setSubscriptionCost(10.0d);
        subscriptions.setSubscriptionId(123L);
        subscriptions.setSubscriptionName("yyyy-MM-dd");

        UserSubs userSubs = new UserSubs();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setLastPaidDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setStatus(PaidStatus.PAID);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userSubs.setSubEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        userSubs.setSubscriptions(subscriptions);
        userSubs.setUserId(123L);
        userSubs.setUserSubId(123L);

        ArrayList<UserSubs> userSubsList = new ArrayList<>();
        userSubsList.add(userSubs);
        doNothing().when(userSubsRepository).delete((UserSubs) any());
        when(userSubsRepository.findAllBySubEndDateBefore((Date) any())).thenReturn(userSubsList);
        doNothing().when(userSubsRepository).deleteAll((Iterable<UserSubs>) any());
        assertThrows(ResourceException.class, () -> subscriberServiceImpl.deleteExpiredSubscriptionOnEndDate());
        verify(subscriberRepository).findById((Long) any());
        verify(userSubsRepository).findAllBySubEndDateBefore((Date) any());
    }

}

