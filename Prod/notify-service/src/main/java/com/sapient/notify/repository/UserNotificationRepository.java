package com.sapient.notify.repository;

import com.sapient.notify.model.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    public Page<UserNotification> findAllByUserId(Long userId, Pageable pageable);

}
