//package com.hrms.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "departments")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Department {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "department_id")
//	private Long departmentId;
//
//	@NotBlank(message = "Department name is required")
//	@Size(max = 100)
//	@Column(name = "department_name", nullable = false, unique = true, length = 100)
//	private String departmentName;
//
//	@NotBlank(message = "Department code is required")
//	@Size(max = 10)
//	@Column(name = "department_code", nullable = false, unique = true, length = 10)
//	private String departmentCode;
//
//	@Column(name = "department_head", length = 100)
//	private String departmentHead;
//
//	@Column(name = "description", columnDefinition = "TEXT")
//	private String description;
//
//	@Column(name = "location", length = 150)
//	private String location;
//
//	@Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$", message = "Invalid phone number format")
//	@Column(name = "contact_number", length = 20)
//	private String contactNumber;
//
//	@Email(message = "Invalid email format")
//	@Column(name = "email", length = 100)
//	private String email;
//
//	@Enumerated(EnumType.STRING)
//	@Column(name = "status", length = 20)
//	private Status status = Status.ACTIVE;
//
//	@Column(name = "created_date", updatable = false)
//	private LocalDateTime createdDate;
//
//	@Column(name = "updated_date")
//	private LocalDateTime updatedDate;
//
//	public enum Status {
//		ACTIVE, INACTIVE
//	}
//
//	@PrePersist
//	protected void onCreate() {
//		this.createdDate = LocalDateTime.now();
//		this.updatedDate = LocalDateTime.now();
//		if (this.status == null) {
//			this.status = Status.ACTIVE;
//		}
//	}
//
//	@PreUpdate
//	protected void onUpdate() {
//		this.updatedDate = LocalDateTime.now();
//	}
//}

package com.hrms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "department_id")
	private Long departmentId;

	@NotBlank(message = "Department name is required")
	@Size(max = 100)
	@Column(name = "department_name", nullable = false, unique = true, length = 100)
	private String departmentName;

	@NotBlank(message = "Department code is required")
	@Size(max = 10)
	@Column(name = "department_code", nullable = false, unique = true, length = 10)
	private String departmentCode;

	// Changed from String to a proper relation
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_head_id")
	private Employee departmentHead;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "location", length = 150)
	private String location;

	@Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$", message = "Invalid phone number format")
	@Column(name = "contact_number", length = 20)
	private String contactNumber;

	@Email(message = "Invalid email format")
	@Column(name = "email", length = 100)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20)
	private Status status = Status.ACTIVE;

	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	public enum Status {
		ACTIVE, INACTIVE
	}

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
		if (this.status == null) {
			this.status = Status.ACTIVE;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now();
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public Employee getDepartmentHead() {
		return departmentHead;
	}

	public void setDepartmentHead(Employee departmentHead) {
		this.departmentHead = departmentHead;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}