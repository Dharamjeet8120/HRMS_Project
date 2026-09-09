package com.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payrolls", uniqueConstraints = @UniqueConstraint(columnNames = { "employee_id", "pay_month",
		"pay_year" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payroll_id")
	private Long payrollId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@NotNull(message = "Pay month is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "pay_month", nullable = false, length = 15)
	private Month payMonth;

	@NotNull(message = "Pay year is required")
	@Column(name = "pay_year", nullable = false)
	private Integer payYear;

	@Column(name = "basic_salary", nullable = false)
	private Double basicSalary;

	@Column(name = "allowances")
	private Double allowances = 0.0;

	@Column(name = "deductions")
	private Double deductions = 0.0;

	@Column(name = "total_working_days")
	private Integer totalWorkingDays;

	@Column(name = "present_days")
	private Integer presentDays;

	@Column(name = "gross_salary")
	private Double grossSalary;

	@Column(name = "net_salary")
	private Double netSalary;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private Status status = Status.GENERATED;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "remarks", length = 255)
	private String remarks;

	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	public enum Status {
		GENERATED, PAID, ON_HOLD
	}

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
		if (this.status == null) {
			this.status = Status.GENERATED;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now();
	}

	public Long getPayrollId() {
		return payrollId;
	}

	public void setPayrollId(Long payrollId) {
		this.payrollId = payrollId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Month getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(Month payMonth) {
		this.payMonth = payMonth;
	}

	public Integer getPayYear() {
		return payYear;
	}

	public void setPayYear(Integer payYear) {
		this.payYear = payYear;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getAllowances() {
		return allowances;
	}

	public void setAllowances(Double allowances) {
		this.allowances = allowances;
	}

	public Double getDeductions() {
		return deductions;
	}

	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}

	public Integer getTotalWorkingDays() {
		return totalWorkingDays;
	}

	public void setTotalWorkingDays(Integer totalWorkingDays) {
		this.totalWorkingDays = totalWorkingDays;
	}

	public Integer getPresentDays() {
		return presentDays;
	}

	public void setPresentDays(Integer presentDays) {
		this.presentDays = presentDays;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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