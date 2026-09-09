package com.hrms.controller;

import java.time.Month;
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

import com.hrms.dto.GeneratePayrollRequestDTO;
import com.hrms.dto.PayrollDTO;
import com.hrms.dto.PayrollMapper;
import com.hrms.entity.Payroll;
import com.hrms.service.PayrollService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payroll")
@CrossOrigin("http://localhost:5173")
public class PayrollController {

	private final PayrollService payrollService;

	public PayrollController(PayrollService payrollService) {
		this.payrollService = payrollService;
	}

	@PostMapping("/generate")
	public ResponseEntity<PayrollDTO> generatePayroll(@Valid @RequestBody GeneratePayrollRequestDTO requestDTO) {
		Payroll payroll = payrollService.generatePayroll(requestDTO.employeeId(), requestDTO.payMonth(),
				requestDTO.payYear(), requestDTO.allowances(), requestDTO.otherDeductions(),
				requestDTO.totalWorkingDays());
		return new ResponseEntity<>(PayrollMapper.toDTO(payroll), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<PayrollDTO>> getAllPayrolls() {
		List<PayrollDTO> payrolls = payrollService.getAllPayrolls().stream().map(PayrollMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(payrolls);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PayrollDTO> getPayrollById(@PathVariable Long id) {
		Payroll payroll = payrollService.getPayrollById(id);
		return ResponseEntity.ok(PayrollMapper.toDTO(payroll));
	}

	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<PayrollDTO>> getPayrollsByEmployee(@PathVariable Long employeeId) {
		List<PayrollDTO> payrolls = payrollService.getPayrollsByEmployee(employeeId).stream().map(PayrollMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(payrolls);
	}

	@GetMapping("/employee/{employeeId}/month/{payMonth}/year/{payYear}")
	public ResponseEntity<PayrollDTO> getPayrollByEmployeeAndMonth(@PathVariable Long employeeId,
			@PathVariable Month payMonth, @PathVariable Integer payYear) {
		Payroll payroll = payrollService.getPayrollByEmployeeAndMonth(employeeId, payMonth, payYear);
		return ResponseEntity.ok(PayrollMapper.toDTO(payroll));
	}

	@GetMapping("/month/{payMonth}/year/{payYear}")
	public ResponseEntity<List<PayrollDTO>> getPayrollsByMonthAndYear(@PathVariable Month payMonth,
			@PathVariable Integer payYear) {
		List<PayrollDTO> payrolls = payrollService.getPayrollsByMonthAndYear(payMonth, payYear).stream()
				.map(PayrollMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(payrolls);
	}

	@GetMapping("/year/{payYear}")
	public ResponseEntity<List<PayrollDTO>> getPayrollsByYear(@PathVariable Integer payYear) {
		List<PayrollDTO> payrolls = payrollService.getPayrollsByYear(payYear).stream().map(PayrollMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(payrolls);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<PayrollDTO>> getPayrollsByStatus(@PathVariable Payroll.Status status) {
		List<PayrollDTO> payrolls = payrollService.getPayrollsByStatus(status).stream().map(PayrollMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(payrolls);
	}

	@PutMapping("/{id}/mark-paid")
	public ResponseEntity<PayrollDTO> markAsPaid(@PathVariable Long id) {
		Payroll payroll = payrollService.markAsPaid(id);
		return ResponseEntity.ok(PayrollMapper.toDTO(payroll));
	}

	@PutMapping("/{id}/hold")
	public ResponseEntity<PayrollDTO> holdPayroll(@PathVariable Long id,
			@RequestParam(required = false) String remarks) {
		Payroll payroll = payrollService.holdPayroll(id, remarks);
		return ResponseEntity.ok(PayrollMapper.toDTO(payroll));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePayroll(@PathVariable Long id) {
		payrollService.deletePayroll(id);
		return ResponseEntity.ok("Payroll record deleted successfully");
	}
}