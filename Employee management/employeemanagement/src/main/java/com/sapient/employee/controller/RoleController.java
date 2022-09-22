package com.sapient.employee.controller;

import java.util.List;

import com.sapient.employee.entity.Role;
import com.sapient.employee.entity.User;
import com.sapient.employee.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getUsers() {
		try {
			//System.out.println(userService.getUsers());
			return new ResponseEntity<>(roleRepo.findAll(), HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
