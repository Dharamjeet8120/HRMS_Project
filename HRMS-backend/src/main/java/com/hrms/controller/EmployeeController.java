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

import com.hrms.dto.EmployeeDTO;
import com.hrms.dto.EmployeeMapper;
import com.hrms.entity.Employee;
import com.hrms.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin("http://localhost:5173")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
		Employee employee = EmployeeMapper.toEntity(employeeDTO);
		Employee saved = employeeService.createEmployee(employee);
		return new ResponseEntity<>(EmployeeMapper.toDTO(saved), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		List<EmployeeDTO> employees = employeeService.getAllEmployees().stream().map(EmployeeMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
		Employee employee = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeDTO employeeDTO) {
		Employee employee = EmployeeMapper.toEntity(employeeDTO);
		Employee updated = employeeService.updateEmployee(id, employee);
		return ResponseEntity.ok(EmployeeMapper.toDTO(updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.ok("Employee deleted successfully");
	}

	@GetMapping("/search")
	public ResponseEntity<List<EmployeeDTO>> searchByName(@RequestParam String name) {
		List<EmployeeDTO> employees = employeeService.searchByName(name).stream().map(EmployeeMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/code/{employeeCode}")
	public ResponseEntity<EmployeeDTO> getEmployeeByCode(@PathVariable String employeeCode) {
		Employee employee = employeeService.getEmployeeByCode(employeeCode);
		return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
	}

	@GetMapping("/department/{departmentId}")
	public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable Long departmentId) {
		List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(departmentId).stream()
				.map(EmployeeMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/designation/{designation}")
	public ResponseEntity<List<EmployeeDTO>> getEmployeesByDesignation(@PathVariable String designation) {
		List<EmployeeDTO> employees = employeeService.getEmployeesByDesignation(designation).stream()
				.map(EmployeeMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<EmployeeDTO>> getEmployeesByStatus(@PathVariable Employee.Status status) {
		List<EmployeeDTO> employees = employeeService.getEmployeesByStatus(status).stream().map(EmployeeMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(employees);
	}
}