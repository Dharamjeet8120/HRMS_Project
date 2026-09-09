//package com.hrms.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.hrms.entity.Department;
//
//@Repository
//public interface DepartmentRepository extends JpaRepository<Department, Long> {
//	Optional<Department> findByDepartmentCode(String departmentCode);
//	boolean existsByDepartmentCode(String departmentCode);
//	List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);
//	List<Department> findByDepartmentHeadContainingIgnoreCase(String departmentHead);
//	Optional<Department> findByDepartmentName(String departmentName);
//	boolean existsByDepartmentName(String departmentName);
//	List<Department> findByLocationContainingIgnoreCase(String location);
//	Optional<Department> findByEmail(String email);
//	boolean existsByEmail(String email);
//}

package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	Optional<Department> findByDepartmentCode(String departmentCode);

	boolean existsByDepartmentCode(String departmentCode);

	List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);

	Optional<Department> findByDepartmentName(String departmentName);

	boolean existsByDepartmentName(String departmentName);

	List<Department> findByLocationContainingIgnoreCase(String location);

	Optional<Department> findByEmail(String email);

	boolean existsByEmail(String email);

	// Naya method - departmentHead ab Employee entity hai
	Optional<Department> findByDepartmentHead_EmployeeId(Long employeeId);
}