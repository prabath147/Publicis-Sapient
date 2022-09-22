package com.sapient.employee.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sapient.employee.entity.Address;
import com.sapient.employee.entity.Department;
import com.sapient.employee.entity.Grade;
import com.sapient.employee.entity.Role;
import com.sapient.employee.entity.User;
import com.sapient.employee.entity.UserDetails;
import com.sapient.employee.repository.AddressRepository;
import com.sapient.employee.repository.DepartmentRepository;
import com.sapient.employee.repository.GradeRepository;
import com.sapient.employee.repository.RoleRepository;
import com.sapient.employee.repository.UserDetailsRepository;
import com.sapient.employee.repository.UserRepository;
import com.sapient.employee.service.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserDetailsRepository userDetailRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	DepartmentRepository departmentRepo;
	
	@Autowired
	AddressRepository addressRepo;
	
	@Autowired
	GradeRepository gradeRepo;
	
	@Autowired 
	RoleRepository roleRepo;

	@Override
	public UserDetails getUserDetailByUserId(long userId) {
		Optional<User> user=userRepo.findById(userId);
		UserDetails userDetail=userDetailRepo.findByUserId(user.get());
		
		return userDetail;
	}

	@Override
	public List<UserDetails> getUserDetails() {
		List<UserDetails> userDetailsList=new ArrayList<>();
		
		userDetailRepo.findAll().forEach(userDetailsList::add);
		
		return userDetailsList;
	}

	@Override
	public UserDetails createUserDetail(UserDetails userDetails) {
		UserDetails userData=new UserDetails();
		Optional<User> user=userRepo.findById(userDetails.getUserId().getUserId());
		
		
		Optional<Role> _role=roleRepo.findById(userDetails.getUserId().getRoleId().getRoleId());
		
		user.get().setRoleId(_role.get());
		userRepo.save(user.get());
		userData.setUserId(user.get());
		userData.setUserFirstname(userDetails.getUserFirstname());
		userData.setUserLastname(userDetails.getUserLastname());
		
		userData.setUserDob(userDetails.getUserDob());
		userData.setUserDoj(userDetails.getUserDoj());
		userData.setUserEmail(userDetails.getUserEmail());
		userData.setUserPhoneno(userDetails.getUserPhoneno());
		Optional<User> manager=userRepo.findById(userDetails.getManagerId().getUserId());
		userData.setManagerId(manager.get());
		Optional<Department> dept=departmentRepo.findById(userDetails.getDeptId().getDeptId());
		userData.setDeptId(dept.get());
		userData.setUserGender(userDetails.getUserGender());
		userData.setUserDesignation(userDetails.getUserDesignation());
		Address address=new Address();
		address.setCity(userDetails.getUserAddressId().getCity());
		address.setState(userDetails.getUserAddressId().getState());
		address.setArea(userDetails.getUserAddressId().getArea());
		address.setPincode(userDetails.getUserAddressId().getPincode());
		addressRepo.save(address);
		userData.setUserAddressId(address);
		userData.setMaritalStatus(userDetails.getMaritalStatus());
		
		Grade grade=gradeRepo.findById(userDetails.getGradeId().getId());
		
		userData.setGradeId(grade);
		
		userDetailRepo.save(userData);
		return userData;
		
	}

	@Override
	public UserDetails updateUserDetails(UserDetails userDetails) {
		Optional<User> user=userRepo.findById(userDetails.getUserId().getUserId());
		UserDetails userData=userDetailRepo.findByUserId(user.get());		
		
		Grade grade=gradeRepo.findById(userDetails.getGradeId().getId());
		
		userData.setGradeId(grade);
		
		
		Optional<Role> _role=roleRepo.findById(userDetails.getUserId().getRoleId().getRoleId());
		
		user.get().setRoleId(_role.get());
		userRepo.save(user.get());
		userDetailRepo.save(userData);
		
		return userData;
	}

	

}
