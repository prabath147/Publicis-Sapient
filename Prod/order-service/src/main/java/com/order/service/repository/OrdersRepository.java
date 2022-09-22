package com.order.service.repository;

import com.order.service.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findAllByUserId(Long userId, Pageable pageable);

    List<Orders> findAllByOrderIdIn(List<Long> orderIdList);

    List<Orders> findAllByUserIdEquals(Long userId);
}
