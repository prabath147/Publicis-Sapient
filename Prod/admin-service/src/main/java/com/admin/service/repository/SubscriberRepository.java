package com.admin.service.repository;

import com.admin.service.entity.Subscriber;
import com.admin.service.entity.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber,Long> {

}
