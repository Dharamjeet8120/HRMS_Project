package com.hrms.service;

import java.util.List;

import com.hrms.entity.Employee;

public interface EmployeeService {
	Employee createEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployeeById(Long id);

	Employee updateEmployee(Long id, Employee updatedData);

	void deleteEmployee(Long id);

	List<Employee> searchByName(String name);

	Employee getEmployeeByCode(String employeeCode);

	List<Employee> getEmployeesByDepartment(Long departmentId);

	List<Employee> getEmployeesByDesignation(String designation);

	List<Employee> getEmployeesByStatus(Employee.Status status);
}