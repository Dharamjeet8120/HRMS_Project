package com.hrms.dto;

import java.time.Month;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GeneratePayrollRequestDTO(@NotNull(message = "Employee id is required") Long employeeId,

		@NotNull(message = "Pay month is required") Month payMonth,

		@NotNull(message = "Pay year is required") Integer payYear,

		@PositiveOrZero(message = "Allowances cannot be negative") Double allowances,

		@PositiveOrZero(message = "Deductions cannot be negative") Double otherDeductions,

		@Positive(message = "Total working days must be positive") Integer totalWorkingDays) {
}