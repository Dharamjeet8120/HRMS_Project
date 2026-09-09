package com.hrms.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.LeaveDTO;
import com.hrms.dto.LeaveMapper;
import com.hrms.entity.LeaveRequest;
import com.hrms.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/leaves")
@CrossOrigin("http://localhost:5173")
public class LeaveController {

	private final LeaveService leaveService;

	public LeaveController(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	@PostMapping
	public ResponseEntity<LeaveDTO> applyLeave(@Valid @RequestBody LeaveDTO leaveDTO) {
		LeaveRequest leaveRequest = LeaveMapper.toEntity(leaveDTO);
		LeaveRequest saved = leaveService.applyLeave(leaveRequest);
		return new ResponseEntity<>(LeaveMapper.toDTO(saved), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<LeaveDTO>> getAllLeaveRequests() {
		List<LeaveDTO> leaves = leaveService.getAllLeaveRequests().stream().map(LeaveMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(leaves);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveDTO> getLeaveById(@PathVariable Long id) {
		LeaveRequest leaveRequest = leaveService.getLeaveById(id);
		return ResponseEntity.ok(LeaveMapper.toDTO(leaveRequest));
	}

	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<LeaveDTO>> getLeavesByEmployee(@PathVariable Long employeeId) {
		List<LeaveDTO> leaves = leaveService.getLeavesByEmployee(employeeId).stream().map(LeaveMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(leaves);
	}

	@GetMapping("/employee/{employeeId}/status/{status}")
	public ResponseEntity<List<LeaveDTO>> getLeavesByEmployeeAndStatus(@PathVariable Long employeeId,
			@PathVariable LeaveRequest.Status status) {
		List<LeaveDTO> leaves = leaveService.getLeavesByEmployeeAndStatus(employeeId, status).stream()
				.map(LeaveMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(leaves);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<LeaveDTO>> getLeavesByStatus(@PathVariable LeaveRequest.Status status) {
		List<LeaveDTO> leaves = leaveService.getLeavesByStatus(status).stream().map(LeaveMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(leaves);
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<LeaveDTO> approveLeave(@PathVariable Long id, @RequestParam String approvedBy) {
		LeaveRequest leaveRequest = leaveService.approveLeave(id, approvedBy);
		return ResponseEntity.ok(LeaveMapper.toDTO(leaveRequest));
	}

	@PutMapping("/{id}/reject")
	public ResponseEntity<LeaveDTO> rejectLeave(@PathVariable Long id, @RequestParam String approvedBy,
			@RequestParam(required = false) String rejectionReason) {
		LeaveRequest leaveRequest = leaveService.rejectLeave(id, approvedBy, rejectionReason);
		return ResponseEntity.ok(LeaveMapper.toDTO(leaveRequest));
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<LeaveDTO> cancelLeave(@PathVariable Long id) {
		LeaveRequest leaveRequest = leaveService.cancelLeave(id);
		return ResponseEntity.ok(LeaveMapper.toDTO(leaveRequest));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteLeave(@PathVariable Long id) {
		leaveService.deleteLeave(id);
		return ResponseEntity.ok("Leave request deleted successfully");
	}
}