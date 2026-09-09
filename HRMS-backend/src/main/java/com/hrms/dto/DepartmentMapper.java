//package com.hrms.dto;
//
//import com.hrms.entity.Department;
//
//public class DepartmentMapper {
//
//	private DepartmentMapper() {
//		// utility class
//	}
//
//	public static DepartmentDTO toDTO(Department department) {
//		if (department == null) {
//			return null;
//		}
//		return new DepartmentDTO(
//				department.getDepartmentId(),
//				department.getDepartmentName(),
//				department.getDepartmentCode(),
//				department.getDepartmentHead(),
//				department.getDescription(),
//				department.getLocation(),
//				department.getContactNumber(),
//				department.getEmail(),
//				department.getStatus(),
//				department.getCreatedDate(),
//				department.getUpdatedDate()
//		);
//	}
//
//	public static Department toEntity(DepartmentDTO dto) {
//		if (dto == null) {
//			return null;
//		}
//		Department department = new Department();
//		department.setDepartmentId(dto.departmentId());
//		department.setDepartmentName(dto.departmentName());
//		department.setDepartmentCode(dto.departmentCode());
//		department.setDepartmentHead(dto.departmentHead());
//		department.setDescription(dto.description());
//		department.setLocation(dto.location());
//		department.setContactNumber(dto.contactNumber());
//		department.setEmail(dto.email());
//		if (dto.status() != null) {
//			department.setStatus(dto.status());
//		}
//		return department;
//	}
//}

package com.hrms.dto;

import com.hrms.entity.Department;
import com.hrms.entity.Employee;

public class DepartmentMapper {

	private DepartmentMapper() {
		// utility class
	}

	public static DepartmentDTO toDTO(Department department) {
		if (department == null) {
			return null;
		}
		Employee head = department.getDepartmentHead();
		String headName = head != null ? head.getFirstName() + " " + head.getLastName() : null;

		return new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName(),
				department.getDepartmentCode(), head != null ? head.getEmployeeId() : null, headName,
				department.getDescription(), department.getLocation(), department.getContactNumber(),
				department.getEmail(), department.getStatus(), department.getCreatedDate(),
				department.getUpdatedDate());
	}

	public static Department toEntity(DepartmentDTO dto) {
		if (dto == null) {
			return null;
		}
		Department department = new Department();
		department.setDepartmentId(dto.departmentId());
		department.setDepartmentName(dto.departmentName());
		department.setDepartmentCode(dto.departmentCode());
		department.setDescription(dto.description());
		department.setLocation(dto.location());
		department.setContactNumber(dto.contactNumber());
		department.setEmail(dto.email());
		if (dto.status() != null) {
			department.setStatus(dto.status());
		}

		if (dto.departmentHeadId() != null) {
			Employee head = new Employee();
			head.setEmployeeId(dto.departmentHeadId());
			department.setDepartmentHead(head);
		}

		return department;
	}
}