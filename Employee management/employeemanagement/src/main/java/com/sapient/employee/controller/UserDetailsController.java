package com.sapient.employee.controller;

import java.util.List;

import javax.validation.Valid;

import com.sapient.employee.entity.LeaveHistory;
import com.sapient.employee.entity.User;
import com.sapient.employee.entity.UserDetails;
import com.sapient.employee.service.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/userdetails")
public class UserDetailsController {
	
	@Autowired
	UserDetailsService userDetailService;
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDetails> getUserDetailByUserId(@PathVariable("id") long id) {
		try {
			return new ResponseEntity<>(userDetailService.getUserDetailByUserId(id), HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/data")
	public ResponseEntity<List<UserDetails>>  getAllUserDetails() {
		try {
			
			List<UserDetails> userDetailList=userDetailService.getUserDetails();
			
			return new ResponseEntity<>(userDetailList, HttpStatus.OK);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/userdata") 
	public ResponseEntity<UserDetails> createUserDetails(@Valid @RequestBody UserDetails userDetails) {
		try {
			
			return new ResponseEntity<>(userDetailService.createUserDetail(userDetails), HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/userData") 
	public ResponseEntity<UserDetails> updateUserDetails(@Valid @RequestBody UserDetails userDetails) {
		try {
			
			return new ResponseEntity<>(userDetailService.updateUserDetails(userDetails), HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
