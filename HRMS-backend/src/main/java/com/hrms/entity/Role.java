package com.hrms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long roleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", nullable = false, unique = true, length = 30)
	private RoleName roleName;
	
	

	public Long getRoleId() {
		return roleId;
	}



	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}



	public RoleName getRoleName() {
		return roleName;
	}



	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}



	public enum RoleName {
		ROLE_ADMIN, ROLE_HR, ROLE_EMPLOYEE
	}
}