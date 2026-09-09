package com.hrms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.entity.LeaveRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaveDTO(Long leaveId,

		@NotNull(message = "Employee id is required") Long employeeId,

		String employeeName,

		@NotNull(message = "Leave type is required") LeaveRequest.LeaveType leaveType,

		@NotNull(message = "Start date is required") LocalDate startDate,

		@NotNull(message = "End date is required") LocalDate endDate,

		Long totalDays,

		@NotBlank(message = "Reason is required") String reason,

		LeaveRequest.Status status,

		String approvedBy,

		String rejectionReason,

		LocalDateTime appliedDate,

		LocalDateTime updatedDate) {
}