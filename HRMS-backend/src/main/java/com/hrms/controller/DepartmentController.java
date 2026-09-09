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
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.DepartmentDTO;
import com.hrms.dto.DepartmentMapper;
import com.hrms.entity.Department;
import com.hrms.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/departments")
@CrossOrigin("http://localhost:5173")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@PostMapping
	public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
		Department department = DepartmentMapper.toEntity(departmentDTO);
		Department saved = departmentService.createDepartment(department);
		return new ResponseEntity<>(DepartmentMapper.toDTO(saved), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
		List<DepartmentDTO> departments = departmentService.getAllDepartments().stream().map(DepartmentMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(departments);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
		Department department = departmentService.getDepartmentById(id);
		return ResponseEntity.ok(DepartmentMapper.toDTO(department));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id,
			@Valid @RequestBody DepartmentDTO departmentDTO) {
		Department department = DepartmentMapper.toEntity(departmentDTO);
		Department updated = departmentService.updateDepartment(id, department);
		return ResponseEntity.ok(DepartmentMapper.toDTO(updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok("Department deleted successfully");
	}

	@GetMapping("/search/name/{name}")
	public ResponseEntity<List<DepartmentDTO>> searchByDepartmentName(@PathVariable String name) {
		List<DepartmentDTO> departments = departmentService.searchByDepartmentName(name).stream()
				.map(DepartmentMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(departments);
	}

	@GetMapping("/search/code/{code}")
	public ResponseEntity<DepartmentDTO> searchByDepartmentCode(@PathVariable String code) {
		Department department = departmentService.searchByDepartmentCode(code);
		return ResponseEntity.ok(DepartmentMapper.toDTO(department));
	}

	@GetMapping("/search/head/{head}")
	public ResponseEntity<List<DepartmentDTO>> searchByDepartmentHead(@PathVariable String head) {
		List<DepartmentDTO> departments = departmentService.searchByDepartmentHead(head).stream()
				.map(DepartmentMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(departments);
	}
}