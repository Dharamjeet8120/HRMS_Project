package com.hrms.service;

import java.time.LocalDate;
import java.util.List;

import com.hrms.entity.Attendance;

public interface AttendanceService {
	Attendance markAttendance(Attendance attendance);

	List<Attendance> getAllAttendance();

	Attendance getAttendanceById(Long id);

	Attendance updateAttendance(Long id, Attendance updatedData);

	void deleteAttendance(Long id);

	List<Attendance> getAttendanceByEmployee(Long employeeId);

	Attendance getAttendanceByEmployeeAndDate(Long employeeId, LocalDate date);

	List<Attendance> getAttendanceByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);

	List<Attendance> getAttendanceByDate(LocalDate date);

	List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate);

	List<Attendance> getAttendanceByStatus(Attendance.Status status);

	Attendance checkOut(Long employeeId, LocalDate date);

	long countPresentDays(Long employeeId, LocalDate startDate, LocalDate endDate);
}