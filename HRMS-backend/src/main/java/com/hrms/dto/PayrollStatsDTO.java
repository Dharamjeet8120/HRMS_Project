package com.hrms.dto;

public record PayrollStatsDTO(long totalPayrollsThisMonth, long paidThisMonth, long onHoldThisMonth,
		double totalNetSalaryPaidThisMonth) {
}