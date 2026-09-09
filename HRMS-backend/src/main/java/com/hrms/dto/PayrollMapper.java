package com.hrms.dto;

import com.hrms.entity.Employee;
import com.hrms.entity.Payroll;

public class PayrollMapper {

	private PayrollMapper() {
		// utility class
	}

	public static PayrollDTO toDTO(Payroll payroll) {
		if (payroll == null) {
			return null;
		}
		Employee employee = payroll.getEmployee();
		String employeeName = employee != null ? employee.getFirstName() + " " + employee.getLastName() : null;

		return new PayrollDTO(payroll.getPayrollId(), employee != null ? employee.getEmployeeId() : null, employeeName,
				payroll.getPayMonth(), payroll.getPayYear(), payroll.getBasicSalary(), payroll.getAllowances(),
				payroll.getDeductions(), payroll.getTotalWorkingDays(), payroll.getPresentDays(),
				payroll.getGrossSalary(), payroll.getNetSalary(), payroll.getStatus(), payroll.getPaymentDate(),
				payroll.getRemarks(), payroll.getCreatedDate(), payroll.getUpdatedDate());
	}
}