package com.hrms.serviceimpl;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hrms.dto.AttendanceStatsDTO;
import com.hrms.dto.DashboardSummaryDTO;
import com.hrms.dto.DepartmentEmployeeCountDTO;
import com.hrms.dto.DepartmentStatsDTO;
import com.hrms.dto.EmployeeStatsDTO;
import com.hrms.dto.LeaveStatsDTO;
import com.hrms.dto.PayrollStatsDTO;
import com.hrms.entity.Attendance;
import com.hrms.entity.Department;
import com.hrms.entity.Employee;
import com.hrms.entity.LeaveRequest;
import com.hrms.entity.Payroll;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveRepository;
import com.hrms.repository.PayrollRepository;
import com.hrms.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final AttendanceRepository attendanceRepository;
	private final LeaveRepository leaveRepository;
	private final PayrollRepository payrollRepository;

	public DashboardServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
			AttendanceRepository attendanceRepository, LeaveRepository leaveRepository,
			PayrollRepository payrollRepository) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.attendanceRepository = attendanceRepository;
		this.leaveRepository = leaveRepository;
		this.payrollRepository = payrollRepository;
	}

	@Override
	public DashboardSummaryDTO getDashboardSummary() {
		List<DepartmentEmployeeCountDTO> employeesPerDepartment = departmentRepository.findAll().stream()
				.map(dept -> new DepartmentEmployeeCountDTO(dept.getDepartmentId(), dept.getDepartmentName(),
						employeeRepository.findByDepartment_DepartmentId(dept.getDepartmentId()).size()))
				.collect(Collectors.toList());

		return new DashboardSummaryDTO(getEmployeeStats(), getDepartmentStats(), getAttendanceStatsForToday(),
				getLeaveStatsForCurrentMonth(), getPayrollStatsForCurrentMonth(), employeesPerDepartment);
	}

	@Override
	public EmployeeStatsDTO getEmployeeStats() {
		List<Employee> allEmployees = employeeRepository.findAll();

		long total = allEmployees.size();
		long active = allEmployees.stream().filter(e -> e.getStatus() == Employee.Status.ACTIVE).count();
		long inactive = allEmployees.stream().filter(e -> e.getStatus() == Employee.Status.INACTIVE).count();
		long onLeave = allEmployees.stream().filter(e -> e.getStatus() == Employee.Status.ON_LEAVE).count();

		return new EmployeeStatsDTO(total, active, inactive, onLeave);
	}

	@Override
	public DepartmentStatsDTO getDepartmentStats() {
		List<Department> allDepartments = departmentRepository.findAll();

		long total = allDepartments.size();
		long active = allDepartments.stream().filter(d -> d.getStatus() == Department.Status.ACTIVE).count();
		long withoutHead = allDepartments.stream().filter(d -> d.getDepartmentHead() == null).count();

		return new DepartmentStatsDTO(total, active, withoutHead);
	}

	@Override
	public AttendanceStatsDTO getAttendanceStatsForToday() {
		LocalDate today = LocalDate.now();
		List<Attendance> todayAttendance = attendanceRepository.findByAttendanceDate(today);

		long present = todayAttendance.stream().filter(a -> a.getStatus() == Attendance.Status.PRESENT).count();
		long absent = todayAttendance.stream().filter(a -> a.getStatus() == Attendance.Status.ABSENT).count();
		long onLeave = todayAttendance.stream().filter(a -> a.getStatus() == Attendance.Status.ON_LEAVE).count();
		long halfDay = todayAttendance.stream().filter(a -> a.getStatus() == Attendance.Status.HALF_DAY).count();

		long totalEmployees = employeeRepository.count();
		double percentage = totalEmployees > 0 ? Math.round((present * 10000.0) / totalEmployees) / 100.0 : 0.0;

		return new AttendanceStatsDTO(present, absent, onLeave, halfDay, percentage);
	}

	@Override
	public LeaveStatsDTO getLeaveStatsForCurrentMonth() {
		YearMonth currentMonth = YearMonth.now();
		LocalDate monthStart = currentMonth.atDay(1);
		LocalDate monthEnd = currentMonth.atEndOfMonth();

		List<LeaveRequest> allLeaves = leaveRepository.findAll();

		List<LeaveRequest> thisMonthLeaves = allLeaves.stream()
				.filter(l -> !l.getStartDate().isBefore(monthStart) && !l.getStartDate().isAfter(monthEnd))
				.collect(Collectors.toList());

		long pending = allLeaves.stream().filter(l -> l.getStatus() == LeaveRequest.Status.PENDING).count();
		long approvedThisMonth = thisMonthLeaves.stream().filter(l -> l.getStatus() == LeaveRequest.Status.APPROVED)
				.count();
		long rejectedThisMonth = thisMonthLeaves.stream().filter(l -> l.getStatus() == LeaveRequest.Status.REJECTED)
				.count();

		return new LeaveStatsDTO(pending, approvedThisMonth, rejectedThisMonth, thisMonthLeaves.size());
	}

	@Override
	public PayrollStatsDTO getPayrollStatsForCurrentMonth() {
		Month currentMonth = LocalDate.now().getMonth();
		Integer currentYear = LocalDate.now().getYear();

		List<Payroll> thisMonthPayrolls = payrollRepository.findByPayMonthAndPayYear(currentMonth, currentYear);

		long total = thisMonthPayrolls.size();
		long paid = thisMonthPayrolls.stream().filter(p -> p.getStatus() == Payroll.Status.PAID).count();
		long onHold = thisMonthPayrolls.stream().filter(p -> p.getStatus() == Payroll.Status.ON_HOLD).count();

		double totalPaid = thisMonthPayrolls.stream().filter(p -> p.getStatus() == Payroll.Status.PAID)
				.mapToDouble(Payroll::getNetSalary).sum();

		return new PayrollStatsDTO(total, paid, onHold, Math.round(totalPaid * 100.0) / 100.0);
	}
}