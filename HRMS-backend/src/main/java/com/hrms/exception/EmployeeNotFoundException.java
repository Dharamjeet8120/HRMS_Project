package com.hrms.exception;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(String message) {
		super(message);
	}

	public EmployeeNotFoundException(Long employeeId) {
		super("Employee not found with id: " + employeeId);
	}
}