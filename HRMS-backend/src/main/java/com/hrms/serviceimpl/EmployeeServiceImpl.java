package com.hrms.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hrms.entity.Department;
import com.hrms.entity.Employee;
import com.hrms.exception.EmployeeNotFoundException;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	@Transactional
	public Employee createEmployee(Employee employee) {
		if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
			throw new DuplicateResourceException("Employee code '" + employee.getEmployeeCode() + "' already exists");
		}
		if (employeeRepository.existsByEmail(employee.getEmail())) {
			throw new DuplicateResourceException("Email '" + employee.getEmail() + "' already in use");
		}

		// Ensure the department actually exists before linking
		Long departmentId = employee.getDepartment() != null ? employee.getDepartment().getDepartmentId() : null;
		if (departmentId == null) {
			throw new ResourceNotFoundException("Department id must be provided for employee");
		}
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		employee.setDepartment(department);
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@Override
	@Transactional
	public Employee updateEmployee(Long id, Employee updatedData) {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

		if (!employee.getEmployeeCode().equalsIgnoreCase(updatedData.getEmployeeCode())
				&& employeeRepository.existsByEmployeeCode(updatedData.getEmployeeCode())) {
			throw new DuplicateResourceException(
					"Employee code '" + updatedData.getEmployeeCode() + "' already exists");
		}
		if (!employee.getEmail().equalsIgnoreCase(updatedData.getEmail())
				&& employeeRepository.existsByEmail(updatedData.getEmail())) {
			throw new DuplicateResourceException("Email '" + updatedData.getEmail() + "' already in use");
		}

		employee.setFirstName(updatedData.getFirstName());
		employee.setLastName(updatedData.getLastName());
		employee.setEmail(updatedData.getEmail());
		employee.setPhoneNumber(updatedData.getPhoneNumber());
		employee.setEmployeeCode(updatedData.getEmployeeCode());
		employee.setDesignation(updatedData.getDesignation());
		employee.setDateOfJoining(updatedData.getDateOfJoining());
		employee.setDateOfBirth(updatedData.getDateOfBirth());
		employee.setGender(updatedData.getGender());
		employee.setAddress(updatedData.getAddress());
		employee.setSalary(updatedData.getSalary());
		if (updatedData.getStatus() != null) {
			employee.setStatus(updatedData.getStatus());
		}

		// Update department only if a different one is provided
		if (updatedData.getDepartment() != null && updatedData.getDepartment().getDepartmentId() != null) {
			Long newDeptId = updatedData.getDepartment().getDepartmentId();
			if (!employee.getDepartment().getDepartmentId().equals(newDeptId)) {
				Department department = departmentRepository.findById(newDeptId)
						.orElseThrow(() -> new ResourceNotFoundException("Department", "id", newDeptId));
				employee.setDepartment(department);
			}
		}

		return employeeRepository.save(employee);
	}

	@Override
	@Transactional
	public void deleteEmployee(Long id) {
		if (!employeeRepository.existsById(id)) {
			throw new EmployeeNotFoundException(id);
		}
		employeeRepository.deleteById(id);
	}

	@Override
	public List<Employee> searchByName(String name) {
		return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
	}

	@Override
	public Employee getEmployeeByCode(String employeeCode) {
		return employeeRepository.findByEmployeeCode(employeeCode)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with code: " + employeeCode));
	}

	@Override
	public List<Employee> getEmployeesByDepartment(Long departmentId) {
		if (!departmentRepository.existsById(departmentId)) {
			throw new ResourceNotFoundException("Department", "id", departmentId);
		}
		return employeeRepository.findByDepartment_DepartmentId(departmentId);
	}

	@Override
	public List<Employee> getEmployeesByDesignation(String designation) {
		return employeeRepository.findByDesignationContainingIgnoreCase(designation);
	}

	@Override
	public List<Employee> getEmployeesByStatus(Employee.Status status) {
		return employeeRepository.findByStatus(status);
	}
}