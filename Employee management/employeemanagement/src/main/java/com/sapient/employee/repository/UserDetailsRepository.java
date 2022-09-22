package com.sapient.employee.repository;

import java.util.List;

import com.sapient.employee.entity.User;
import com.sapient.employee.entity.UserDetails;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
	
	List<UserDetails> findByManagerId(User managerId);
	
	UserDetails findByUserId(User userId);

}
