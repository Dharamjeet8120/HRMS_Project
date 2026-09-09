package com.hrms.service;

import com.hrms.dto.AttendanceStatsDTO;
import com.hrms.dto.DashboardSummaryDTO;
import com.hrms.dto.DepartmentStatsDTO;
import com.hrms.dto.EmployeeStatsDTO;
import com.hrms.dto.LeaveStatsDTO;
import com.hrms.dto.PayrollStatsDTO;

public interface DashboardService {
	DashboardSummaryDTO getDashboardSummary();

	EmployeeStatsDTO getEmployeeStats();

	DepartmentStatsDTO getDepartmentStats();

	AttendanceStatsDTO getAttendanceStatsForToday();

	LeaveStatsDTO getLeaveStatsForCurrentMonth();

	PayrollStatsDTO getPayrollStatsForCurrentMonth();
}