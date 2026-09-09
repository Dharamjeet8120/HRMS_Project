package com.hrms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.hrms.dto.AttendanceDTO;
import com.hrms.dto.AttendanceMapper;
import com.hrms.entity.Attendance;
import com.hrms.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/attendance")
@CrossOrigin("http://localhost:5173")
public class AttendanceController {

	private final AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostMapping
	public ResponseEntity<AttendanceDTO> markAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
		Attendance attendance = AttendanceMapper.toEntity(attendanceDTO);
		Attendance saved = attendanceService.markAttendance(attendance);
		return new ResponseEntity<>(AttendanceMapper.toDTO(saved), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
		List<AttendanceDTO> attendanceList = attendanceService.getAllAttendance().stream().map(AttendanceMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(attendanceList);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Long id) {
		Attendance attendance = attendanceService.getAttendanceById(id);
		return ResponseEntity.ok(AttendanceMapper.toDTO(attendance));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AttendanceDTO> updateAttendance(@PathVariable Long id,
			@Valid @RequestBody AttendanceDTO attendanceDTO) {
		Attendance attendance = AttendanceMapper.toEntity(attendanceDTO);
		Attendance updated = attendanceService.updateAttendance(id, attendance);
		return ResponseEntity.ok(AttendanceMapper.toDTO(updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
		attendanceService.deleteAttendance(id);
		return ResponseEntity.ok("Attendance record deleted successfully");
	}

	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByEmployee(@PathVariable Long employeeId) {
		List<AttendanceDTO> attendanceList = attendanceService.getAttendanceByEmployee(employeeId).stream()
				.map(AttendanceMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(attendanceList);
	}

	@GetMapping("/employee/{employeeId}/date/{date}")
	public ResponseEntity<AttendanceDTO> getAttendanceByEmployeeAndDate(@PathVariable Long employeeId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		Attendance attendance = attendanceService.getAttendanceByEmployeeAndDate(employeeId, date);
		return ResponseEntity.ok(AttendanceMapper.toDTO(attendance));
	}

	@GetMapping("/employee/{employeeId}/range")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByEmployeeAndDateRange(@PathVariable Long employeeId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		List<AttendanceDTO> attendanceList = attendanceService
				.getAttendanceByEmployeeAndDateRange(employeeId, startDate, endDate).stream()
				.map(AttendanceMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(attendanceList);
	}

	@GetMapping("/date/{date}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<AttendanceDTO> attendanceList = attendanceService.getAttendanceByDate(date).stream()
				.map(AttendanceMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(attendanceList);
	}

	@GetMapping("/range")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByDateRange(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		List<AttendanceDTO> attendanceList = attendanceService.getAttendanceByDateRange(startDate, endDate).stream()
				.map(AttendanceMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(attendanceList);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByStatus(@PathVariable Attendance.Status status) {
		List<AttendanceDTO> attendanceList = attendanceService.getAttendanceByStatus(status).stream()
				.map(AttendanceMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(attendanceList);
	}

	@PutMapping("/checkout/{employeeId}")
	public ResponseEntity<AttendanceDTO> checkOut(@PathVariable Long employeeId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		Attendance attendance = attendanceService.checkOut(employeeId, date);
		return ResponseEntity.ok(AttendanceMapper.toDTO(attendance));
	}

	@GetMapping("/employee/{employeeId}/count-present")
	public ResponseEntity<Long> countPresentDays(@PathVariable Long employeeId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		long count = attendanceService.countPresentDays(employeeId, startDate, endDate);
		return ResponseEntity.ok(count);
	}
}