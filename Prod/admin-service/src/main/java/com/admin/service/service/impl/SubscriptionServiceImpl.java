package com.admin.service.service.impl;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.SubscriptionsDto;
import com.admin.service.entity.SubscriptionStatus;
import com.admin.service.entity.Subscriptions;
import com.admin.service.exception.ResourceException;
import com.admin.service.repository.SubscriptionsRepository;
import com.admin.service.service.SubscriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PageableResponse<SubscriptionsDto> getAllSubscriptions(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Subscriptions> subscriptionsPage = subscriptionsRepository.findAll(requestedPage);
        return getSubscriptionsDtoPageableResponse(subscriptionsPage);
    }

    private PageableResponse<SubscriptionsDto> getSubscriptionsDtoPageableResponse(Page<Subscriptions> subscriptionsPage) {
        List<Subscriptions> allSubscription = subscriptionsPage.getContent();
        List<SubscriptionsDto> subscriptionsDtoList = new ArrayList<>();
        for (Subscriptions subscription : allSubscription)
            subscriptionsDtoList.add(modelMapper.map(subscription, SubscriptionsDto.class));
        PageableResponse<SubscriptionsDto> pageableSubscriptionResponse = new PageableResponse<>();
        return pageableSubscriptionResponse.setResponseData(subscriptionsDtoList, subscriptionsPage);
    }

    @Override
    public PageableResponse<SubscriptionsDto> getAllSubscriptionsByStatus(Integer pageNumber, Integer pageSize, SubscriptionStatus status) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Subscriptions> subscriptionsPage = subscriptionsRepository.findAllByStatus(status, requestedPage);
        return getSubscriptionsDtoPageableResponse(subscriptionsPage);
    }

    @Override
    public boolean checkIfExists(Long subscriptionId) {

        return subscriptionsRepository.existsById(subscriptionId);
    }

    @Override
    public SubscriptionsDto getSubscriptionById(Long subscriptionId) {
        Optional<Subscriptions> subscriptionDto = subscriptionsRepository.findById(subscriptionId);
        if (subscriptionDto.isEmpty()) {
            throw new ResourceException("Subscription", "subscriptionId", subscriptionId, ResourceException.ErrorType.FOUND);
        }
        return modelMapper.map(subscriptionDto.get(), SubscriptionsDto.class);
    }

    @Override
    public SubscriptionsDto createSubscription(SubscriptionsDto subscriptionsDto) throws Exception {
        long var = subscriptionsDto.getEndDate().getTime() - subscriptionsDto.getStartDate().getTime();
        if ((var / (1000 * 60 * 60 * 24)) % 365 >= 1) {
            subscriptionsDto.setStatus(SubscriptionStatus.valueOf("ACTIVE"));
            Subscriptions subscriptions = this.modelMapper.map(subscriptionsDto, Subscriptions.class);
            try {
                return this.modelMapper.map(this.subscriptionsRepository.save(subscriptions), SubscriptionsDto.class);
            } catch (Exception e) {
                throw new ResourceException("Subscription", "Id", subscriptions.getSubscriptionId(), ResourceException.ErrorType.CREATED);

            }
        } else
            throw new RuntimeException("Enter Valid Subscription Dates!");
    }

    @Override
    public String deleteSubscription(Long subscriptionId) {
        Optional<Subscriptions> subscription = this.subscriptionsRepository.findById(subscriptionId);
        if (subscription.isEmpty()) {
            throw new ResourceException("Subscription", "subscriptionId", subscriptionId, ResourceException.ErrorType.FOUND);
        }

        Date endDate = subscription.get().getEndDate();       // get the end date
        Date currentDate = new Date();      // get the current date

        long endDateTime = endDate.getTime();
        long currentDateTime = currentDate.getTime();
        if (currentDateTime > endDateTime) {
            // if current date > end date then the subscription has expired
            this.subscriptionsRepository.deleteById(subscriptionId);
            return "Deleted successfully";
        }
        else
        {
            return "Delete unsuccessful due to expiration date";
        }
    }
}
