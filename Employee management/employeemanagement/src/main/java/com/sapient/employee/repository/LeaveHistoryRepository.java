package com.sapient.employee.repository;

import java.util.List;

import com.sapient.employee.entity.LeaveHistory;
import com.sapient.employee.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveHistoryRepository extends JpaRepository<LeaveHistory, Long> {
	
	List<LeaveHistory> findByUserLeaveId(User userLeaveId);

}
