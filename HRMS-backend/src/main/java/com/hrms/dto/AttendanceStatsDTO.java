package com.hrms.dto;

public record AttendanceStatsDTO(long presentToday, long absentToday, long onLeaveToday, long halfDayToday,
		double todayAttendancePercentage) {
}