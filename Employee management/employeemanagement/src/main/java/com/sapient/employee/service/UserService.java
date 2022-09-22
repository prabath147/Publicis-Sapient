package com.sapient.employee.service;

import java.util.List;

import com.sapient.employee.entity.User;

public interface UserService {
	
	User createUser(User user);
	
	User getUserById(long id);
	
	List<User> getUsers();
	
	User findByUsernameAndPassword(String username, String password);

}
