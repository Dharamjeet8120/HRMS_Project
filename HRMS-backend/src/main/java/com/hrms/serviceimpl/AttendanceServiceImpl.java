package com.hrms.serviceimpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final EmployeeRepository employeeRepository;

	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
		this.attendanceRepository = attendanceRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	@Transactional
	public Attendance markAttendance(Attendance attendance) {
		Long employeeId = attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeId() : null;
		if (employeeId == null) {
			throw new ResourceNotFoundException("Employee id must be provided for attendance");
		}

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

		if (attendanceRepository.existsByEmployee_EmployeeIdAndAttendanceDate(employeeId,
				attendance.getAttendanceDate())) {
			throw new DuplicateResourceException("Attendance already marked for employee id " + employeeId + " on "
					+ attendance.getAttendanceDate());
		}

		attendance.setEmployee(employee);
		return attendanceRepository.save(attendance);
	}

	@Override
	public List<Attendance> getAllAttendance() {
		return attendanceRepository.findAll();
	}

	@Override
	public Attendance getAttendanceById(Long id) {
		return attendanceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
	}

	@Override
	@Transactional
	public Attendance updateAttendance(Long id, Attendance updatedData) {
		Attendance attendance = attendanceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));

		attendance.setCheckInTime(updatedData.getCheckInTime());
		attendance.setCheckOutTime(updatedData.getCheckOutTime());
		if (updatedData.getStatus() != null) {
			attendance.setStatus(updatedData.getStatus());
		}
		attendance.setRemarks(updatedData.getRemarks());

		return attendanceRepository.save(attendance);
	}

	@Override
	@Transactional
	public void deleteAttendance(Long id) {
		if (!attendanceRepository.existsById(id)) {
			throw new ResourceNotFoundException("Attendance", "id", id);
		}
		attendanceRepository.deleteById(id);
	}

	@Override
	public List<Attendance> getAttendanceByEmployee(Long employeeId) {
		if (!employeeRepository.existsById(employeeId)) {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
		return attendanceRepository.findByEmployee_EmployeeId(employeeId);
	}

	@Override
	public Attendance getAttendanceByEmployeeAndDate(Long employeeId, LocalDate date) {
		return attendanceRepository.findByEmployee_EmployeeIdAndAttendanceDate(employeeId, date)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Attendance not found for employee id " + employeeId + " on " + date));
	}

	@Override
	public List<Attendance> getAttendanceByEmployeeAndDateRange(Long employeeId, LocalDate startDate,
			LocalDate endDate) {
		if (!employeeRepository.existsById(employeeId)) {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
		return attendanceRepository.findByEmployee_EmployeeIdAndAttendanceDateBetween(employeeId, startDate, endDate);
	}

	@Override
	public List<Attendance> getAttendanceByDate(LocalDate date) {
		return attendanceRepository.findByAttendanceDate(date);
	}

	@Override
	public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
		return attendanceRepository.findByAttendanceDateBetween(startDate, endDate);
	}

	@Override
	public List<Attendance> getAttendanceByStatus(Attendance.Status status) {
		return attendanceRepository.findByStatus(status);
	}

	@Override
	@Transactional
	public Attendance checkOut(Long employeeId, LocalDate date) {
		Attendance attendance = attendanceRepository.findByEmployee_EmployeeIdAndAttendanceDate(employeeId, date)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Attendance not found for employee id " + employeeId + " on " + date));

		if (attendance.getCheckOutTime() != null) {
			throw new DuplicateResourceException("Employee id " + employeeId + " has already checked out on " + date);
		}

		attendance.setCheckOutTime(LocalTime.now());
		return attendanceRepository.save(attendance);
	}

	@Override
	public long countPresentDays(Long employeeId, LocalDate startDate, LocalDate endDate) {
		if (!employeeRepository.existsById(employeeId)) {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
		return attendanceRepository.findByEmployee_EmployeeIdAndAttendanceDateBetween(employeeId, startDate, endDate)
				.stream().filter(a -> a.getStatus() == Attendance.Status.PRESENT).count();
	}
}