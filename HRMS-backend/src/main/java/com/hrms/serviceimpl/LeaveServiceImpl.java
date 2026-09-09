package com.hrms.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hrms.entity.Employee;
import com.hrms.entity.LeaveRequest;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveRepository;
import com.hrms.service.LeaveService;

@Service
public class LeaveServiceImpl implements LeaveService {

	private final LeaveRepository leaveRepository;
	private final EmployeeRepository employeeRepository;

	public LeaveServiceImpl(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
		this.leaveRepository = leaveRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	@Transactional
	public LeaveRequest applyLeave(LeaveRequest leaveRequest) {
		Long employeeId = leaveRequest.getEmployee() != null ? leaveRequest.getEmployee().getEmployeeId() : null;
		if (employeeId == null) {
			throw new ResourceNotFoundException("Employee id must be provided for leave request");
		}

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

		if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
			throw new IllegalArgumentException("End date cannot be before start date");
		}

		List<LeaveRequest> overlapping = leaveRepository.findOverlappingApprovedLeaves(employeeId,
				leaveRequest.getStartDate(), leaveRequest.getEndDate());
		if (!overlapping.isEmpty()) {
			throw new DuplicateResourceException(
					"Employee already has an approved leave overlapping with the selected dates");
		}

		leaveRequest.setEmployee(employee);
		leaveRequest.setStatus(LeaveRequest.Status.PENDING);
		return leaveRepository.save(leaveRequest);
	}

	@Override
	public List<LeaveRequest> getAllLeaveRequests() {
		return leaveRepository.findAll();
	}

	@Override
	public LeaveRequest getLeaveById(Long id) {
		return leaveRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", id));
	}

	@Override
	public List<LeaveRequest> getLeavesByEmployee(Long employeeId) {
		if (!employeeRepository.existsById(employeeId)) {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
		return leaveRepository.findByEmployee_EmployeeId(employeeId);
	}

	@Override
	public List<LeaveRequest> getLeavesByEmployeeAndStatus(Long employeeId, LeaveRequest.Status status) {
		if (!employeeRepository.existsById(employeeId)) {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
		return leaveRepository.findByEmployee_EmployeeIdAndStatus(employeeId, status);
	}

	@Override
	public List<LeaveRequest> getLeavesByStatus(LeaveRequest.Status status) {
		return leaveRepository.findByStatus(status);
	}

	@Override
	@Transactional
	public LeaveRequest approveLeave(Long id, String approvedBy) {
		LeaveRequest leaveRequest = leaveRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", id));

		if (leaveRequest.getStatus() != LeaveRequest.Status.PENDING) {
			throw new IllegalStateException(
					"Only PENDING leave requests can be approved. Current status: " + leaveRequest.getStatus());
		}

		leaveRequest.setStatus(LeaveRequest.Status.APPROVED);
		leaveRequest.setApprovedBy(approvedBy);
		return leaveRepository.save(leaveRequest);
	}

	@Override
	@Transactional
	public LeaveRequest rejectLeave(Long id, String approvedBy, String rejectionReason) {
		LeaveRequest leaveRequest = leaveRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", id));

		if (leaveRequest.getStatus() != LeaveRequest.Status.PENDING) {
			throw new IllegalStateException(
					"Only PENDING leave requests can be rejected. Current status: " + leaveRequest.getStatus());
		}

		leaveRequest.setStatus(LeaveRequest.Status.REJECTED);
		leaveRequest.setApprovedBy(approvedBy);
		if (StringUtils.hasText(rejectionReason)) {
			leaveRequest.setRejectionReason(rejectionReason);
		}
		return leaveRepository.save(leaveRequest);
	}

	@Override
	@Transactional
	public LeaveRequest cancelLeave(Long id) {
		LeaveRequest leaveRequest = leaveRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", id));

		if (leaveRequest.getStatus() == LeaveRequest.Status.CANCELLED) {
			throw new IllegalStateException("Leave request is already cancelled");
		}
		if (leaveRequest.getStatus() == LeaveRequest.Status.REJECTED) {
			throw new IllegalStateException("Rejected leave requests cannot be cancelled");
		}

		leaveRequest.setStatus(LeaveRequest.Status.CANCELLED);
		return leaveRepository.save(leaveRequest);
	}

	@Override
	@Transactional
	public void deleteLeave(Long id) {
		if (!leaveRepository.existsById(id)) {
			throw new ResourceNotFoundException("Leave request", "id", id);
		}
		leaveRepository.deleteById(id);
	}
}