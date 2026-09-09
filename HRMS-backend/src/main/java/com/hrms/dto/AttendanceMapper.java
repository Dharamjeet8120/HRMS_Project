package com.hrms.dto;

import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;

public class AttendanceMapper {

	private AttendanceMapper() {
		// utility class
	}

	public static AttendanceDTO toDTO(Attendance attendance) {
		if (attendance == null) {
			return null;
		}
		Employee employee = attendance.getEmployee();
		String employeeName = employee != null ? employee.getFirstName() + " " + employee.getLastName() : null;

		return new AttendanceDTO(attendance.getAttendanceId(), employee != null ? employee.getEmployeeId() : null,
				employeeName, attendance.getAttendanceDate(), attendance.getCheckInTime(), attendance.getCheckOutTime(),
				attendance.getWorkingHours(), attendance.getStatus(), attendance.getRemarks(),
				attendance.getCreatedDate(), attendance.getUpdatedDate());
	}

	public static Attendance toEntity(AttendanceDTO dto) {
		if (dto == null) {
			return null;
		}
		Attendance attendance = new Attendance();
		attendance.setAttendanceId(dto.attendanceId());
		attendance.setAttendanceDate(dto.attendanceDate());
		attendance.setCheckInTime(dto.checkInTime());
		attendance.setCheckOutTime(dto.checkOutTime());
		if (dto.status() != null) {
			attendance.setStatus(dto.status());
		}
		attendance.setRemarks(dto.remarks());

		if (dto.employeeId() != null) {
			Employee employee = new Employee();
			employee.setEmployeeId(dto.employeeId());
			attendance.setEmployee(employee);
		}

		return attendance;
	}
}