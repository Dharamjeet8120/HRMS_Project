package com.hrms.service;

import java.time.Month;
import java.util.List;

import com.hrms.entity.Payroll;

public interface PayrollService {
	Payroll generatePayroll(Long employeeId, Month payMonth, Integer payYear, Double allowances, Double otherDeductions,
			Integer totalWorkingDays);

	List<Payroll> getAllPayrolls();

	Payroll getPayrollById(Long id);

	List<Payroll> getPayrollsByEmployee(Long employeeId);

	Payroll getPayrollByEmployeeAndMonth(Long employeeId, Month payMonth, Integer payYear);

	List<Payroll> getPayrollsByMonthAndYear(Month payMonth, Integer payYear);

	List<Payroll> getPayrollsByYear(Integer payYear);

	List<Payroll> getPayrollsByStatus(Payroll.Status status);

	Payroll markAsPaid(Long id);

	Payroll holdPayroll(Long id, String remarks);

	void deletePayroll(Long id);
}