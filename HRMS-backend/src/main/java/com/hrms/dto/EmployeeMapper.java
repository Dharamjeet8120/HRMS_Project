package com.hrms.dto;

import com.hrms.entity.Department;
import com.hrms.entity.Employee;

public class EmployeeMapper {

	private EmployeeMapper() {
		// utility class
	}

	public static EmployeeDTO toDTO(Employee employee) {
		if (employee == null) {
			return null;
		}
		return new EmployeeDTO(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(),
				employee.getEmail(), employee.getPhoneNumber(), employee.getEmployeeCode(), employee.getDesignation(),
				employee.getDateOfJoining(), employee.getDateOfBirth(), employee.getGender(), employee.getAddress(),
				employee.getSalary(), employee.getStatus(),
				employee.getDepartment() != null ? employee.getDepartment().getDepartmentId() : null,
				employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null,
				employee.getCreatedDate(), employee.getUpdatedDate());
	}

	public static Employee toEntity(EmployeeDTO dto) {
		if (dto == null) {
			return null;
		}
		Employee employee = new Employee();
		employee.setEmployeeId(dto.employeeId());
		employee.setFirstName(dto.firstName());
		employee.setLastName(dto.lastName());
		employee.setEmail(dto.email());
		employee.setPhoneNumber(dto.phoneNumber());
		employee.setEmployeeCode(dto.employeeCode());
		employee.setDesignation(dto.designation());
		employee.setDateOfJoining(dto.dateOfJoining());
		employee.setDateOfBirth(dto.dateOfBirth());
		employee.setGender(dto.gender());
		employee.setAddress(dto.address());
		employee.setSalary(dto.salary());
		if (dto.status() != null) {
			employee.setStatus(dto.status());
		}

		// Only departmentId is needed here — service layer fetches the actual
		// Department
		if (dto.departmentId() != null) {
			Department department = new Department();
			department.setDepartmentId(dto.departmentId());
			employee.setDepartment(department);
		}

		return employee;
	}
}