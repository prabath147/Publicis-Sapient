package com.sapient.employee.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sapient.employee.entity.LeaveHistory;
import com.sapient.employee.entity.LeaveStatus;
import com.sapient.employee.entity.User;
import com.sapient.employee.entity.UserDetails;
import com.sapient.employee.exception.UserNotFoundException;
import com.sapient.employee.repository.LeaveHistoryRepository;
import com.sapient.employee.repository.UserDetailsRepository;
import com.sapient.employee.repository.UserRepository;
import com.sapient.employee.service.LeaveHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveHistoryServiceImpl implements LeaveHistoryService {
	
	@Autowired
	LeaveHistoryRepository leaveRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserDetailsRepository userDetailRepo;

	@Override
	public List<LeaveHistory> getLeaveByUserId(long id) throws UserNotFoundException{
		
			List<LeaveHistory> leaveList=new ArrayList<>();
			Optional<User> user=userRepo.findById(id);
			if(user.get()==null)
				throw new UserNotFoundException("user not found");
			leaveList=leaveRepo.findByUserLeaveId(user.get());
			return leaveList;
		
		
	}

	@Override
	public LeaveHistory createLeave(LeaveHistory leaveHistory) {
		LeaveHistory leave=new LeaveHistory();
		Optional<User> user=userRepo.findById(leaveHistory.getUserLeaveId().getUserId());
		
		leave.setUserLeaveId(user.get());
		leave.setLeaveStatus(LeaveStatus.PENDING);
		leave.setLeaveStartDate(leaveHistory.getLeaveStartDate());
		leave.setLeaveEndDate(leaveHistory.getLeaveEndDate());
		leave.setLeaveNoOfDaysApplied(leaveHistory.getLeaveNoOfDaysApplied());
		
		leaveRepo.save(leave);
		return leave;
		
	}

	@Override
	public List<LeaveHistory> getLeaveHistoryByManagerId(long id) throws UserNotFoundException{
		// TODO Auto-generated method stub
		List<LeaveHistory> leaveHistoryList=new ArrayList<>();
		Optional<User> manager=userRepo.findById(id);
		if(manager.get()==null)
			throw new UserNotFoundException("user not found");
		List<UserDetails> userDetailsList= userDetailRepo.findByManagerId(manager.get());
		if(userDetailsList.size()==0)
			throw new UserNotFoundException("user not found");

		for(UserDetails userDetails:userDetailsList) {
			Optional<User> user=userRepo.findById(userDetails.getUserId().getUserId());
			leaveRepo.findByUserLeaveId(user.get()).forEach(leaveHistoryList::add);
		}
		
		return leaveHistoryList;
	}

	@Override
	public LeaveHistory updateLeaveHistory(long id,LeaveHistory leaveHistory) {
		// TODO Auto-generated method stub
		Optional<LeaveHistory> leave=leaveRepo.findById(id);
		leave.get().setLeaveStatus(leaveHistory.getLeaveStatus());
		leaveRepo.save(leave.get());
		return leave.get();
	}
	
	@Override
	public LeaveHistory getLeaveById(long id) {
		Optional<LeaveHistory> leave=leaveRepo.findById(id);
		if(leave.get()!=null) {
			return leave.get();
		}
		return null;
	}

}
