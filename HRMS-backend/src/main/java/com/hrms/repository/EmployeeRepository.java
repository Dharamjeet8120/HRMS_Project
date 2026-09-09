package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmployeeCode(String employeeCode);

	boolean existsByEmployeeCode(String employeeCode);

	Optional<Employee> findByEmail(String email);

	boolean existsByEmail(String email);

	List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

	List<Employee> findByDepartment_DepartmentId(Long departmentId);

	List<Employee> findByDepartment_DepartmentCode(String departmentCode);

	List<Employee> findByDesignationContainingIgnoreCase(String designation);

	List<Employee> findByStatus(Employee.Status status);

	List<Employee> findByGender(String gender);
}