//package com.hrms.serviceimpl;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import com.hrms.entity.Department;
//import com.hrms.exception.DepartmentNotFoundException;
//import com.hrms.exception.DuplicateResourceException;
//import com.hrms.repository.DepartmentRepository;
//import com.hrms.service.DepartmentService;
//
//@Service
//public class DepartmentServiceImpl implements DepartmentService {
//
//	private final DepartmentRepository departmentRepository;
//
//	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
//		this.departmentRepository = departmentRepository;
//	}
//
//	@Override
//	@Transactional
//	public Department createDepartment(Department department) {
//		if (departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {
//			throw new DuplicateResourceException(
//					"Department code '" + department.getDepartmentCode() + "' already exists");
//		}
//		if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
//			throw new DuplicateResourceException(
//					"Department name '" + department.getDepartmentName() + "' already exists");
//		}
//		if (StringUtils.hasText(department.getEmail()) && departmentRepository.existsByEmail(department.getEmail())) {
//			throw new DuplicateResourceException("Email '" + department.getEmail() + "' already in use");
//		}
//		return departmentRepository.save(department);
//	}
//
//	@Override
//	public List<Department> getAllDepartments() {
//		return departmentRepository.findAll();
//	}
//
//	@Override
//	public Department getDepartmentById(Long id) {
//		return departmentRepository.findById(id)
//				.orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
//	}
//
//	@Override
//	@Transactional
//	public Department updateDepartment(Long id, Department updatedData) {
//		Department department = departmentRepository.findById(id)
//				.orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
//
//		// Check duplicates only if the value actually changed
//		if (!department.getDepartmentCode().equalsIgnoreCase(updatedData.getDepartmentCode())
//				&& departmentRepository.existsByDepartmentCode(updatedData.getDepartmentCode())) {
//			throw new DuplicateResourceException(
//					"Department code '" + updatedData.getDepartmentCode() + "' already exists");
//		}
//		if (!department.getDepartmentName().equalsIgnoreCase(updatedData.getDepartmentName())
//				&& departmentRepository.existsByDepartmentName(updatedData.getDepartmentName())) {
//			throw new DuplicateResourceException(
//					"Department name '" + updatedData.getDepartmentName() + "' already exists");
//		}
//
//		department.setDepartmentName(updatedData.getDepartmentName());
//		department.setDepartmentCode(updatedData.getDepartmentCode());
//		department.setDepartmentHead(updatedData.getDepartmentHead());
//		department.setDescription(updatedData.getDescription());
//		department.setLocation(updatedData.getLocation());
//		department.setContactNumber(updatedData.getContactNumber());
//		department.setEmail(updatedData.getEmail());
//		if (updatedData.getStatus() != null) {
//			department.setStatus(updatedData.getStatus());
//		}
//		return departmentRepository.save(department);
//	}
//
//	@Override
//	@Transactional
//	public void deleteDepartment(Long id) {
//		if (!departmentRepository.existsById(id)) {
//			throw new DepartmentNotFoundException("Department not found with id: " + id);
//		}
//		departmentRepository.deleteById(id);
//	}
//
//	@Override
//	public List<Department> searchByDepartmentName(String departmentName) {
//		return departmentRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
//	}
//
//	@Override
//	public Department searchByDepartmentCode(String departmentCode) {
//		return departmentRepository.findByDepartmentCode(departmentCode).orElseThrow(
//				() -> new DepartmentNotFoundException("Department not found with code: " + departmentCode));
//	}
//
//	@Override
//	public List<Department> searchByDepartmentHead(String departmentHead) {
//		return departmentRepository.findByDepartmentHeadContainingIgnoreCase(departmentHead);
//	}
//}

package com.hrms.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hrms.entity.Department;
import com.hrms.entity.Employee;
import com.hrms.exception.DepartmentNotFoundException;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final EmployeeRepository employeeRepository;

	public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
		this.departmentRepository = departmentRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	@Transactional
	public Department createDepartment(Department department) {
		if (departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {
			throw new DuplicateResourceException(
					"Department code '" + department.getDepartmentCode() + "' already exists");
		}
		if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
			throw new DuplicateResourceException(
					"Department name '" + department.getDepartmentName() + "' already exists");
		}
		if (StringUtils.hasText(department.getEmail()) && departmentRepository.existsByEmail(department.getEmail())) {
			throw new DuplicateResourceException("Email '" + department.getEmail() + "' already in use");
		}

		resolveDepartmentHead(department);

		return departmentRepository.save(department);
	}

	@Override
	public List<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}

	@Override
	public Department getDepartmentById(Long id) {
		return departmentRepository.findById(id)
				.orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
	}

	@Override
	@Transactional
	public Department updateDepartment(Long id, Department updatedData) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

		if (!department.getDepartmentCode().equalsIgnoreCase(updatedData.getDepartmentCode())
				&& departmentRepository.existsByDepartmentCode(updatedData.getDepartmentCode())) {
			throw new DuplicateResourceException(
					"Department code '" + updatedData.getDepartmentCode() + "' already exists");
		}
		if (!department.getDepartmentName().equalsIgnoreCase(updatedData.getDepartmentName())
				&& departmentRepository.existsByDepartmentName(updatedData.getDepartmentName())) {
			throw new DuplicateResourceException(
					"Department name '" + updatedData.getDepartmentName() + "' already exists");
		}

		department.setDepartmentName(updatedData.getDepartmentName());
		department.setDepartmentCode(updatedData.getDepartmentCode());
		department.setDescription(updatedData.getDescription());
		department.setLocation(updatedData.getLocation());
		department.setContactNumber(updatedData.getContactNumber());
		department.setEmail(updatedData.getEmail());
		if (updatedData.getStatus() != null) {
			department.setStatus(updatedData.getStatus());
		}

		if (updatedData.getDepartmentHead() != null) {
			department.setDepartmentHead(updatedData.getDepartmentHead());
			resolveDepartmentHead(department);
		}

		return departmentRepository.save(department);
	}

	@Override
	@Transactional
	public void deleteDepartment(Long id) {
		if (!departmentRepository.existsById(id)) {
			throw new DepartmentNotFoundException("Department not found with id: " + id);
		}
		departmentRepository.deleteById(id);
	}

	@Override
	public List<Department> searchByDepartmentName(String departmentName) {
		return departmentRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
	}

	@Override
	public Department searchByDepartmentCode(String departmentCode) {
		return departmentRepository.findByDepartmentCode(departmentCode).orElseThrow(
				() -> new DepartmentNotFoundException("Department not found with code: " + departmentCode));
	}

	@Override
	public List<Department> searchByDepartmentHead(String departmentHeadName) {
		// departmentHead ab Employee hai — naam se search karne ke liye employee
		// dhoondte hain
		return departmentRepository.findAll().stream()
				.filter(d -> d.getDepartmentHead() != null
						&& (d.getDepartmentHead().getFirstName() + " " + d.getDepartmentHead().getLastName())
								.toLowerCase().contains(departmentHeadName.toLowerCase()))
				.toList();
	}

	private void resolveDepartmentHead(Department department) {
		if (department.getDepartmentHead() != null && department.getDepartmentHead().getEmployeeId() != null) {
			Long employeeId = department.getDepartmentHead().getEmployeeId();
			Employee employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
			department.setDepartmentHead(employee);
		}
	}
}