package com.hrms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	Optional<Attendance> findByEmployee_EmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

	boolean existsByEmployee_EmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

	List<Attendance> findByEmployee_EmployeeId(Long employeeId);

	List<Attendance> findByEmployee_EmployeeIdAndAttendanceDateBetween(Long employeeId, LocalDate startDate,
			LocalDate endDate);

	List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

	List<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

	List<Attendance> findByStatus(Attendance.Status status);

	List<Attendance> findByEmployee_EmployeeIdAndStatus(Long employeeId, Attendance.Status status);

	long countByEmployee_EmployeeIdAndStatus(Long employeeId, Attendance.Status status);
}