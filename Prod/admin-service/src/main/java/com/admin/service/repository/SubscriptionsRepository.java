package com.admin.service.repository;

import com.admin.service.entity.SubscriptionStatus;
import com.admin.service.entity.Subscriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions,Long> {
    Page<Subscriptions> findAllByStatus(SubscriptionStatus subscriptionStatus, Pageable requestedPage);

    List<Subscriptions> findAllByEndDateBefore(Date endDate);


}
