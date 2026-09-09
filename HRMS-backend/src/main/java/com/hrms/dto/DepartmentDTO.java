//package com.hrms.dto;
//
//import java.time.LocalDateTime;
//
//import com.hrms.entity.Department;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//
//public record DepartmentDTO(Long departmentId,
//
//		@NotBlank(message = "Department name is required") @Size(max = 100) String departmentName,
//
//		@NotBlank(message = "Department code is required") @Size(max = 10) String departmentCode,
//
//		String departmentHead,
//
//		String description,
//
//		String location,
//
//		@Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$", message = "Invalid phone number format") String contactNumber,
//
//		@Email(message = "Invalid email format") String email,
//
//		Department.Status status,
//
//		LocalDateTime createdDate,
//
//		LocalDateTime updatedDate) {
//}

package com.hrms.dto;

import java.time.LocalDateTime;

import com.hrms.entity.Department;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DepartmentDTO(Long departmentId,

		@NotBlank(message = "Department name is required") @Size(max = 100) String departmentName,

		@NotBlank(message = "Department code is required") @Size(max = 10) String departmentCode,

		Long departmentHeadId,

		String departmentHeadName,

		String description,

		String location,

		@Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$", message = "Invalid phone number format") String contactNumber,

		@Email(message = "Invalid email format") String email,

		Department.Status status,

		LocalDateTime createdDate,

		LocalDateTime updatedDate) {
}