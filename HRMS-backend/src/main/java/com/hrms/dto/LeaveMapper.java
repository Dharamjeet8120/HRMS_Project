package com.hrms.dto;

import com.hrms.entity.Employee;
import com.hrms.entity.LeaveRequest;

public class LeaveMapper {

	private LeaveMapper() {
		// utility class
	}

	public static LeaveDTO toDTO(LeaveRequest leaveRequest) {
		if (leaveRequest == null) {
			return null;
		}
		Employee employee = leaveRequest.getEmployee();
		String employeeName = employee != null ? employee.getFirstName() + " " + employee.getLastName() : null;

		return new LeaveDTO(leaveRequest.getLeaveId(), employee != null ? employee.getEmployeeId() : null, employeeName,
				leaveRequest.getLeaveType(), leaveRequest.getStartDate(), leaveRequest.getEndDate(),
				leaveRequest.getTotalDays(), leaveRequest.getReason(), leaveRequest.getStatus(),
				leaveRequest.getApprovedBy(), leaveRequest.getRejectionReason(), leaveRequest.getAppliedDate(),
				leaveRequest.getUpdatedDate());
	}

	public static LeaveRequest toEntity(LeaveDTO dto) {
		if (dto == null) {
			return null;
		}
		LeaveRequest leaveRequest = new LeaveRequest();
		leaveRequest.setLeaveId(dto.leaveId());
		leaveRequest.setLeaveType(dto.leaveType());
		leaveRequest.setStartDate(dto.startDate());
		leaveRequest.setEndDate(dto.endDate());
		leaveRequest.setReason(dto.reason());
		if (dto.status() != null) {
			leaveRequest.setStatus(dto.status());
		}

		if (dto.employeeId() != null) {
			Employee employee = new Employee();
			employee.setEmployeeId(dto.employeeId());
			leaveRequest.setEmployee(employee);
		}

		return leaveRequest;
	}
}