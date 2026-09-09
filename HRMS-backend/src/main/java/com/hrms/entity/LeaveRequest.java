package com.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_id")
	private Long leaveId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@Enumerated(EnumType.STRING)
	@Column(name = "leave_type", nullable = false, length = 20)
	private LeaveType leaveType;

	@NotNull(message = "Start date is required")
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@NotNull(message = "End date is required")
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "total_days")
	private Long totalDays;

	@NotBlank(message = "Reason is required")
	@Column(name = "reason", length = 500)
	private String reason;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private Status status = Status.PENDING;

	@Column(name = "approved_by", length = 100)
	private String approvedBy;

	@Column(name = "rejection_reason", length = 500)
	private String rejectionReason;

	@Column(name = "applied_date", updatable = false)
	private LocalDateTime appliedDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	public enum LeaveType {
		CASUAL, SICK, EARNED, MATERNITY, PATERNITY, UNPAID
	}

	public enum Status {
		PENDING, APPROVED, REJECTED, CANCELLED
	}

	@PrePersist
	protected void onCreate() {
		this.appliedDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
		if (this.status == null) {
			this.status = Status.PENDING;
		}
		calculateTotalDays();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now();
		calculateTotalDays();
	}

	private void calculateTotalDays() {
		if (startDate != null && endDate != null) {
			this.totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		}
	}

	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Long getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(Long totalDays) {
		this.totalDays = totalDays;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public LocalDateTime getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(LocalDateTime appliedDate) {
		this.appliedDate = appliedDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

}