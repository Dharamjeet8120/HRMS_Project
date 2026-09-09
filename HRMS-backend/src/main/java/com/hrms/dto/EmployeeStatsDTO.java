package com.hrms.dto;

public record EmployeeStatsDTO(long totalEmployees, long activeEmployees, long inactiveEmployees,
		long onLeaveEmployees) {
}