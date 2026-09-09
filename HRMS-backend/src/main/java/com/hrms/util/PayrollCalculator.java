package com.hrms.util;

import org.springframework.stereotype.Component;

@Component
public class PayrollCalculator {

	/**
	 * Calculates the gross salary = basic salary + allowances.
	 */
	public double calculateGrossSalary(double basicSalary, double allowances) {
		return basicSalary + allowances;
	}

	/**
	 * Calculates a per-day salary based on total working days in the month.
	 */
	public double calculatePerDaySalary(double basicSalary, int totalWorkingDays) {
		if (totalWorkingDays <= 0) {
			throw new IllegalArgumentException("Total working days must be greater than zero");
		}
		return basicSalary / totalWorkingDays;
	}

	/**
	 * Calculates a deduction for absent days (basic salary lost for days not
	 * present).
	 */
	public double calculateAbsenceDeduction(double basicSalary, int totalWorkingDays, int presentDays) {
		int absentDays = totalWorkingDays - presentDays;
		if (absentDays <= 0) {
			return 0.0;
		}
		double perDaySalary = calculatePerDaySalary(basicSalary, totalWorkingDays);
		return round(perDaySalary * absentDays);
	}

	/**
	 * Calculates the final net salary: net = (basicSalary + allowances) -
	 * absenceDeduction - otherDeductions
	 */
	public double calculateNetSalary(double basicSalary, double allowances, double otherDeductions,
			int totalWorkingDays, int presentDays) {
		double grossSalary = calculateGrossSalary(basicSalary, allowances);
		double absenceDeduction = calculateAbsenceDeduction(basicSalary, totalWorkingDays, presentDays);
		double netSalary = grossSalary - absenceDeduction - otherDeductions;
		return round(Math.max(netSalary, 0.0));
	}

	/**
	 * Rounds a value to 2 decimal places.
	 */
	public double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}