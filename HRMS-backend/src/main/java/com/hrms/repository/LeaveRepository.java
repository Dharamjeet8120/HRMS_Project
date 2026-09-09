package com.hrms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.entity.LeaveRequest;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

	List<LeaveRequest> findByEmployee_EmployeeId(Long employeeId);

	List<LeaveRequest> findByEmployee_EmployeeIdAndStatus(Long employeeId, LeaveRequest.Status status);

	List<LeaveRequest> findByStatus(LeaveRequest.Status status);

	List<LeaveRequest> findByLeaveType(LeaveRequest.LeaveType leaveType);

	List<LeaveRequest> findByEmployee_EmployeeIdAndLeaveType(Long employeeId, LeaveRequest.LeaveType leaveType);

	List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

	@org.springframework.data.jpa.repository.Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.employeeId = :employeeId "
			+ "AND lr.status = 'APPROVED' " + "AND ((lr.startDate BETWEEN :startDate AND :endDate) "
			+ "OR (lr.endDate BETWEEN :startDate AND :endDate) "
			+ "OR (:startDate BETWEEN lr.startDate AND lr.endDate))")
	List<LeaveRequest> findOverlappingApprovedLeaves(
			@org.springframework.data.repository.query.Param("employeeId") Long employeeId,
			@org.springframework.data.repository.query.Param("startDate") LocalDate startDate,
			@org.springframework.data.repository.query.Param("endDate") LocalDate endDate);

	long countByEmployee_EmployeeIdAndLeaveTypeAndStatus(Long employeeId, LeaveRequest.LeaveType leaveType,
			LeaveRequest.Status status);
}