package com.hrms.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterDTO(@NotBlank(message = "Username is required") @Size(min = 4, max = 50) String username,

		@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,

		@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

		@NotEmpty(message = "At least one role is required") Set<String> roles, // e.g. ["ROLE_HR"], ["ROLE_ADMIN"]

		Long employeeId // optional - link to an existing Employee record
) {
}