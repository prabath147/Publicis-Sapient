package com.order.service.repository;

import java.time.LocalDate;
import java.util.List;

import com.order.service.model.RepeatOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatOrderRepository extends JpaRepository<RepeatOrder,Long> {
	
	List<RepeatOrder> findAllByDeliveryDate(LocalDate date);
	
	Page<RepeatOrder> findAllByUserId(Long userId, Pageable requestedPage);

	List<RepeatOrder> findAllByUserId(Long userId);
	
	List<RepeatOrder> findAllByUserIdEquals(Long userId);
	
	
}
