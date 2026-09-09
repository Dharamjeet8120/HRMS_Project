package com.hrms.serviceimpl;

import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import com.hrms.entity.Payroll;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.PayrollRepository;
import com.hrms.service.PayrollService;
import com.hrms.util.PayrollCalculator;

@Service
public class PayrollServiceImpl implements PayrollService {

	private final PayrollRepository payrollRepository;
	private final EmployeeRepository employeeRepository;
	private final AttendanceRepository attendanceRepository;
	private final PayrollCalculator payrollCalculator;

	public PayrollServiceImpl(PayrollRepository payrollRepository, EmployeeRepository employeeRepository,
			AttendanceRepository attendanceRepository, PayrollCalculator payrollCalculator) {
		this.payrollRepository = payrollRepository;
		this.employeeRepository = employeeRepository;
		this.attendanceRepository = attendanceRepository;
		this.payrollCalculator = payrollCalculator;
	}

	@Override
	@Transactional
	public Payroll generatePayroll(Long employeeId, Month payMonth, Integer payYear, Double allowances,
			Double otherDeductions, Integer totalWorkingDays) {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

		if (payrollRepository.existsByEmployee_EmployeeIdAndPayMonthAndPayYear(employeeId, payMonth, payYear)) {
			throw new DuplicateResourceException(
					"Payroll already generated for employee id " + employeeId + " for " + payMonth + " " + payYear);
		}

		if (employee.getSalary() == null) {
			throw new IllegalArgumentException("Employee does not have a basic salary configured");
		}

		int workingDays = (totalWorkingDays != null) ? totalWorkingDays : 26; // default fallback

		java.time.LocalDate startDate = java.time.LocalDate.of(payYear, payMonth, 1);
		java.time.LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

		List<Attendance> attendanceRecords = attendanceRepository
				.findByEmployee_EmployeeIdAndAttendanceDateBetween(employeeId, startDate, endDate);

		long presentDaysCount = attendanceRecords.stream()
				.filter(a -> a.getStatus() == Attendance.Status.PRESENT || a.getStatus() == Attendance.Status.HALF_DAY)
				.count();

		double basicSalary = employee.getSalary();
		double allowanceAmount = allowances != null ? allowances : 0.0;
		double deductionAmount = otherDeductions != null ? otherDeductions : 0.0;

		double grossSalary = payrollCalculator.calculateGrossSalary(basicSalary, allowanceAmount);
		double netSalary = payrollCalculator.calculateNetSalary(basicSalary, allowanceAmount, deductionAmount,
				workingDays, (int) presentDaysCount);

		Payroll payroll = new Payroll();
		payroll.setEmployee(employee);
		payroll.setPayMonth(payMonth);
		payroll.setPayYear(payYear);
		payroll.setBasicSalary(basicSalary);
		payroll.setAllowances(allowanceAmount);
		payroll.setDeductions(deductionAmount);
		payroll.setTotalWorkingDays(workingDays);
		payroll.setPresentDays((int) presentDaysCount);
		payroll.setGrossSalary(payrollCalculator.round(grossSalary));
		payroll.setNetSalary(netSalary);
		payroll.setStatus(Payroll.Status.GENERATED);

		return payrollRepository.save(payroll);
	}

	@Override
	public List<Payroll> getAllPayrolls() {
		return payrollRepository.findAll();
	}

	@Override
	public Payroll getPayrollById(Long id) {
		return payrollRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", id));
	}

	@Override
	public List<Payroll> getPayrollsByEmployee(Long employeeId) {
		if (!employeeRepository.existsById(employeeId)) {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
		return payrollRepository.findByEmployee_EmployeeId(employeeId);
	}

	@Override
	public Payroll getPayrollByEmployeeAndMonth(Long employeeId, Month payMonth, Integer payYear) {
		return payrollRepository.findByEmployee_EmployeeIdAndPayMonthAndPayYear(employeeId, payMonth, payYear)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Payroll not found for employee id " + employeeId + " for " + payMonth + " " + payYear));
	}

	@Override
	public List<Payroll> getPayrollsByMonthAndYear(Month payMonth, Integer payYear) {
		return payrollRepository.findByPayMonthAndPayYear(payMonth, payYear);
	}

	@Override
	public List<Payroll> getPayrollsByYear(Integer payYear) {
		return payrollRepository.findByPayYear(payYear);
	}

	@Override
	public List<Payroll> getPayrollsByStatus(Payroll.Status status) {
		return payrollRepository.findByStatus(status);
	}

	@Override
	@Transactional
	public Payroll markAsPaid(Long id) {
		Payroll payroll = payrollRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", id));

		if (payroll.getStatus() == Payroll.Status.PAID) {
			throw new IllegalStateException("Payroll is already marked as PAID");
		}

		payroll.setStatus(Payroll.Status.PAID);
		payroll.setPaymentDate(java.time.LocalDate.now());
		return payrollRepository.save(payroll);
	}

	@Override
	@Transactional
	public Payroll holdPayroll(Long id, String remarks) {
		Payroll payroll = payrollRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", id));

		if (payroll.getStatus() == Payroll.Status.PAID) {
			throw new IllegalStateException("A PAID payroll cannot be put on hold");
		}

		payroll.setStatus(Payroll.Status.ON_HOLD);
		payroll.setRemarks(remarks);
		return payrollRepository.save(payroll);
	}

	@Override
	@Transactional
	public void deletePayroll(Long id) {
		if (!payrollRepository.existsById(id)) {
			throw new ResourceNotFoundException("Payroll", "id", id);
		}
		payrollRepository.deleteById(id);
	}
}