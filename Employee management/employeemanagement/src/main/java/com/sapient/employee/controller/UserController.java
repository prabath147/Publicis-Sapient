package com.sapient.employee.controller;

import java.util.List;

import javax.validation.Valid;


import com.sapient.employee.entity.User;
import com.sapient.employee.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/user") 
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		try {
			User user1= userService.createUser(user);
			return new ResponseEntity<>(user1, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
		try {
			//System.out.println(userService.getUserById(id));
			return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		try {
			//System.out.println(userService.getUsers());
			return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/authenticate/{username}/{password}")
	public ResponseEntity<User> getUserByAuthentication(@PathVariable("username") String username,@PathVariable("password") String password) {
		try {
			
			//System.out.println(userService.getUserById(id))
			User user1=userService.findByUsernameAndPassword(username,password);
			return new ResponseEntity<>(user1, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
