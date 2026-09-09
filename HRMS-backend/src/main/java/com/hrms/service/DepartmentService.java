package com.hrms.service;

import java.util.List;

import com.hrms.entity.Department;

public interface DepartmentService {
	Department createDepartment(Department department);

	List<Department> getAllDepartments();

	Department getDepartmentById(Long id);

	Department updateDepartment(Long id, Department updatedData);

	void deleteDepartment(Long id);

	List<Department> searchByDepartmentName(String departmentName);

	Department searchByDepartmentCode(String departmentCode);

	List<Department> searchByDepartmentHead(String departmentHead);
}