package com.hrms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.AttendanceStatsDTO;
import com.hrms.dto.DashboardSummaryDTO;
import com.hrms.dto.DepartmentStatsDTO;
import com.hrms.dto.EmployeeStatsDTO;
import com.hrms.dto.LeaveStatsDTO;
import com.hrms.dto.PayrollStatsDTO;
import com.hrms.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin("http://localhost:5173")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/summary")
	public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
		return ResponseEntity.ok(dashboardService.getDashboardSummary());
	}

	@GetMapping("/employees")
	public ResponseEntity<EmployeeStatsDTO> getEmployeeStats() {
		return ResponseEntity.ok(dashboardService.getEmployeeStats());
	}

	@GetMapping("/departments")
	public ResponseEntity<DepartmentStatsDTO> getDepartmentStats() {
		return ResponseEntity.ok(dashboardService.getDepartmentStats());
	}

	@GetMapping("/attendance/today")
	public ResponseEntity<AttendanceStatsDTO> getAttendanceStatsForToday() {
		return ResponseEntity.ok(dashboardService.getAttendanceStatsForToday());
	}

	@GetMapping("/leaves/current-month")
	public ResponseEntity<LeaveStatsDTO> getLeaveStatsForCurrentMonth() {
		return ResponseEntity.ok(dashboardService.getLeaveStatsForCurrentMonth());
	}

	@GetMapping("/payroll/current-month")
	public ResponseEntity<PayrollStatsDTO> getPayrollStatsForCurrentMonth() {
		return ResponseEntity.ok(dashboardService.getPayrollStatsForCurrentMonth());
	}
}