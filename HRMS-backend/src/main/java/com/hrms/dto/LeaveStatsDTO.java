package com.hrms.dto;

public record LeaveStatsDTO(long pendingRequests, long approvedThisMonth, long rejectedThisMonth,
		long totalRequestsThisMonth) {
}