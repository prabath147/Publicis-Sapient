package com.sapient.employee.service;

import java.util.List;

import com.sapient.employee.entity.UserDetails;

public interface UserDetailsService {
	
	UserDetails getUserDetailByUserId(long userId);
	
	
	List<UserDetails> getUserDetails();
	
	UserDetails createUserDetail(UserDetails userDetails);
	
	UserDetails updateUserDetails(UserDetails userDetails);

}
