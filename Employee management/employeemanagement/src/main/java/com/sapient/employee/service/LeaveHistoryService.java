package com.sapient.employee.service;

import java.util.List;

import com.sapient.employee.entity.LeaveHistory;
import com.sapient.employee.entity.LeaveStatus;
import com.sapient.employee.exception.UserNotFoundException;

public interface LeaveHistoryService {
	
	List<LeaveHistory> getLeaveByUserId(long id) throws UserNotFoundException;
	
	LeaveHistory createLeave(LeaveHistory leaveHistory);
	
	List<LeaveHistory> getLeaveHistoryByManagerId(long id) throws UserNotFoundException;
	
	LeaveHistory updateLeaveHistory(long id,LeaveHistory leaveHistory);
	
	LeaveHistory getLeaveById(long id);

}
