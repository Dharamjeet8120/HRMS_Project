package com.hrms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import com.hrms.entity.Payroll;

public record PayrollDTO(Long payrollId,

		Long employeeId,

		String employeeName,

		Month payMonth,

		Integer payYear,

		Double basicSalary,

		Double allowances,

		Double deductions,

		Integer totalWorkingDays,

		Integer presentDays,

		Double grossSalary,

		Double netSalary,

		Payroll.Status status,

		LocalDate paymentDate,

		String remarks,

		LocalDateTime createdDate,

		LocalDateTime updatedDate) {
}