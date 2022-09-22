package com.sapient.employee.service.Impl;

import java.util.List;
import java.util.Optional;

import com.sapient.employee.entity.Role;
import com.sapient.employee.entity.User;
import com.sapient.employee.repository.RoleRepository;
import com.sapient.employee.repository.UserRepository;
import com.sapient.employee.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired 
	RoleRepository roleRepo;

	@Override
	public User createUser(User user) {
		User user1=new User();
		user1.setUsername(user.getUsername());
		user1.setPassword(user.getPassword());
		Optional<Role> _role=roleRepo.findById(1L);
		user1.setRoleId(_role.get());
		userRepo.save(user1);
		return user1;
	}

	@Override
	public User getUserById(long id) {
		Optional<User> user=userRepo.findById(id);
		System.out.println(user.get().toString());
		return user.get();
	}

	@Override
	public List<User> getUsers() {
		List<User> users=userRepo.findAll();
		return users;
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		User _user=userRepo.findByUsernameAndPassword(username,password);
		if(_user!=null) {
			return _user;
		}
		return null;
	}

}
