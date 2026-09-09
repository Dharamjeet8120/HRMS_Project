package com.hrms.service;

import java.util.List;

import com.hrms.entity.LeaveRequest;

public interface LeaveService {
	LeaveRequest applyLeave(LeaveRequest leaveRequest);

	List<LeaveRequest> getAllLeaveRequests();

	LeaveRequest getLeaveById(Long id);

	List<LeaveRequest> getLeavesByEmployee(Long employeeId);

	List<LeaveRequest> getLeavesByEmployeeAndStatus(Long employeeId, LeaveRequest.Status status);

	List<LeaveRequest> getLeavesByStatus(LeaveRequest.Status status);

	LeaveRequest approveLeave(Long id, String approvedBy);

	LeaveRequest rejectLeave(Long id, String approvedBy, String rejectionReason);

	LeaveRequest cancelLeave(Long id);

	void deleteLeave(Long id);
}