package com.hrms.repository;

import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

	Optional<Payroll> findByEmployee_EmployeeIdAndPayMonthAndPayYear(Long employeeId, Month payMonth, Integer payYear);

	boolean existsByEmployee_EmployeeIdAndPayMonthAndPayYear(Long employeeId, Month payMonth, Integer payYear);

	List<Payroll> findByEmployee_EmployeeId(Long employeeId);

	List<Payroll> findByPayMonthAndPayYear(Month payMonth, Integer payYear);

	List<Payroll> findByPayYear(Integer payYear);

	List<Payroll> findByStatus(Payroll.Status status);

	List<Payroll> findByEmployee_EmployeeIdAndPayYear(Long employeeId, Integer payYear);
}