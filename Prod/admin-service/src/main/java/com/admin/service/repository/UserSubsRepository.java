package com.admin.service.repository;

import com.admin.service.entity.Subscriptions;
import com.admin.service.entity.UserSubs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserSubsRepository extends JpaRepository<UserSubs,Long> {
    public List<UserSubs> findAllBySubEndDateBefore(Date date);
    public List<UserSubs> findAllBySubscriptionsIn(List<Subscriptions> subscriptionsList);
}
