package com.hrms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.entity.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeDTO(Long employeeId,

		@NotBlank(message = "First name is required") @Size(max = 50) String firstName,

		@NotBlank(message = "Last name is required") @Size(max = 50) String lastName,

		@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

		@Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$", message = "Invalid phone number format") String phoneNumber,

		@NotBlank(message = "Employee code is required") @Size(max = 20) String employeeCode,

		String designation,

		@NotNull(message = "Date of joining is required") LocalDate dateOfJoining,

		LocalDate dateOfBirth,

		String gender,

		String address,

		Double salary,

		Employee.Status status,

		@NotNull(message = "Department id is required") Long departmentId,

		String departmentName,

		LocalDateTime createdDate,

		LocalDateTime updatedDate) {
}