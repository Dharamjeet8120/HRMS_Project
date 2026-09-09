package com.hrms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.hrms.entity.Attendance;

import jakarta.validation.constraints.NotNull;

public record AttendanceDTO(Long attendanceId,

		@NotNull(message = "Employee id is required") Long employeeId,

		String employeeName,

		@NotNull(message = "Attendance date is required") LocalDate attendanceDate,

		LocalTime checkInTime,

		LocalTime checkOutTime,

		Double workingHours,

		Attendance.Status status,

		String remarks,

		LocalDateTime createdDate,

		LocalDateTime updatedDate) {
}