
package com.admin.service.service.impl;


import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.SubscriptionsDto;
import com.admin.service.entity.*;
import com.admin.service.exception.ResourceException;
import com.admin.service.repository.SubscriptionsRepository;
import com.admin.service.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {
    @InjectMocks
    SubscriptionServiceImpl subscriptionServiceImpl;

    @Mock
    private SubscriptionsRepository subscriptionsRepository;

    @Mock
    private ModelMapper modelMapper;

    Subscriptions mockSubscription;
    SubscriptionsDto mockSubscriptionDto;
    Benefits mockBenefits;
    @BeforeEach
    void setup () throws Exception {
        mockBenefits = new Benefits();
        mockBenefits.setOne_day_delivery(false);
        mockBenefits.setDelivery_discount(10);


        mockSubscriptionDto = SubscriptionsDto.builder()
                .subscriptionId(1L)
                .subscriptionName("Sub Name")
                .description("Desc For testing")
                .startDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-08-08 12:15:00"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-07-08 12:15:00"))
                .subscriptionCost(100.00)
                .status(SubscriptionStatus.EXPIRED)
                .benefits(mockBenefits)
                .period(15)
                .build();

        mockSubscription = Subscriptions.builder()
                .subscriptionId(1L)
                .subscriptionName("Sub Name")
                .description("Desc For testing")
                .startDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-08-08 12:15:00"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-07-08 12:15:00"))
                .subscriptionCost(100.00)
                .status(SubscriptionStatus.EXPIRED)
                .benefits(mockBenefits)
                .period(15)
                .build();
    }

    @Test
    void createSubscription() throws Exception {
        when(subscriptionsRepository.save(any(Subscriptions.class))).thenReturn(mockSubscription);
        when(modelMapper.map(any(), any())).thenReturn(mockSubscription).thenReturn(mockSubscriptionDto);

        SubscriptionsDto mySubscriptionDto = subscriptionServiceImpl.createSubscription(mockSubscriptionDto);
        assertNotNull(mySubscriptionDto);
        assert (1L == mySubscriptionDto.getSubscriptionId());
    }


    @Test
    void createSubscriptionThrowsException() {

        when(subscriptionsRepository.save(any(Subscriptions.class))).thenThrow(new DataAccessResourceFailureException("e"));
        when(modelMapper.map(any(),any())).thenReturn(mockSubscription);

        assertThatThrownBy(() -> subscriptionServiceImpl.createSubscription(mockSubscriptionDto)).isInstanceOf(ResourceException.class);
    }

    @Test
    void findSubscriptionById() {
        when(subscriptionsRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockSubscription));
        when(modelMapper.map(any(Subscriptions.class),any())).thenReturn(mockSubscriptionDto);

        SubscriptionsDto subscriptionsDto = subscriptionServiceImpl.getSubscriptionById(1L);
        Assertions.assertNotNull(subscriptionsDto);
        Assertions.assertEquals("Sub Name",subscriptionsDto.getSubscriptionName());
    }

    @Test
    void findSubscriptionByNonExistingId() {
        when(subscriptionsRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subscriptionServiceImpl.getSubscriptionById(1L)).isInstanceOf(ResourceException.class);
    }


    @Test
    void deleteSubscriptionById() throws ParseException {
        doNothing().when(subscriptionsRepository).deleteById(anyLong());
        when(subscriptionsRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockSubscription));
        subscriptionServiceImpl.deleteSubscription(1L);
        verify(subscriptionsRepository,times(1)).deleteById(1L);
    }

    @Test
    void deleteSubscriptionByNonExistingId()  {
        when(subscriptionsRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subscriptionServiceImpl.deleteSubscription(1L)).isInstanceOf(ResourceException.class);
    }

    @Test
    void checkIfExists()  {
        when(subscriptionsRepository.existsById(anyLong())).thenReturn(true);
        assertTrue(subscriptionServiceImpl.checkIfExists(1L));
    }


    @Test
    void getAllSubscriptions() {
        ArrayList<Subscriptions> subscriptions = new ArrayList<>();
        when(subscriptionsRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(subscriptions));
        PageableResponse<SubscriptionsDto> actualSubscriptions = subscriptionServiceImpl.getAllSubscriptions(0, 1);
        assertEquals(subscriptions, actualSubscriptions.getData());
        assertEquals(0L, actualSubscriptions.getTotalRecords().longValue());
        assertEquals(1, actualSubscriptions.getTotalPages().intValue());
        assertEquals(0, actualSubscriptions.getPageSize().intValue());
        assertEquals(0, actualSubscriptions.getPageNumber().intValue());
        assertTrue(actualSubscriptions.getIsLastPage());
        verify(subscriptionsRepository).findAll((Pageable) any());
    }


    @Test
    void getAllSubscriptionsByStatusExpired() {
        ArrayList<Subscriptions> subscriptions = new ArrayList<>();
        subscriptions.add(mockSubscription);
        when(subscriptionsRepository.findAllByStatus((SubscriptionStatus) any(), (Pageable) any())).thenReturn(new PageImpl<>(subscriptions));
        ArrayList<SubscriptionsDto> subscriptionsDtos = new ArrayList<>();
        subscriptionsDtos.add(mockSubscriptionDto);
        when(modelMapper.map(any(Subscriptions.class),any())).thenReturn(mockSubscriptionDto);


        PageableResponse<SubscriptionsDto> actualSubscriptions = subscriptionServiceImpl.getAllSubscriptionsByStatus(0, 1, SubscriptionStatus.ACTIVE);

        assertEquals(subscriptionsDtos.get(0), actualSubscriptions.getData().get(0));

        assertEquals(1L, actualSubscriptions.getTotalRecords().longValue());
        assertEquals(1, actualSubscriptions.getTotalPages().intValue());
        assertEquals(1, actualSubscriptions.getPageSize().intValue());
        assertEquals(0, actualSubscriptions.getPageNumber().intValue());
        assertTrue(actualSubscriptions.getIsLastPage());
        verify(subscriptionsRepository).findAllByStatus((SubscriptionStatus) any(),(Pageable) any());
    }


    @Test
    void getAllSubscriptionsByStatusActive() {
        ArrayList<Subscriptions> subscriptions = new ArrayList<>();
        when(subscriptionsRepository.findAllByStatus((SubscriptionStatus) any(), (Pageable) any())).thenReturn(new PageImpl<>(subscriptions));

        PageableResponse<SubscriptionsDto> actualSubscriptions = subscriptionServiceImpl.getAllSubscriptionsByStatus(0, 1, SubscriptionStatus.ACTIVE);

        assertEquals(subscriptions, actualSubscriptions.getData());

        assertEquals(0L, actualSubscriptions.getTotalRecords().longValue());
        assertEquals(1, actualSubscriptions.getTotalPages().intValue());
        assertEquals(0, actualSubscriptions.getPageSize().intValue());
        assertEquals(0, actualSubscriptions.getPageNumber().intValue());
        assertTrue(actualSubscriptions.getIsLastPage());
        verify(subscriptionsRepository).findAllByStatus((SubscriptionStatus) any(),(Pageable) any());
    }
}

