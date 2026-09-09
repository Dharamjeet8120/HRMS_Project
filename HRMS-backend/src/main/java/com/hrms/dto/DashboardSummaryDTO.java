package com.hrms.dto;

import java.util.List;

public record DashboardSummaryDTO(EmployeeStatsDTO employeeStats, DepartmentStatsDTO departmentStats,
		AttendanceStatsDTO attendanceStats, LeaveStatsDTO leaveStats, PayrollStatsDTO payrollStats,
		List<DepartmentEmployeeCountDTO> employeesPerDepartment) {
}