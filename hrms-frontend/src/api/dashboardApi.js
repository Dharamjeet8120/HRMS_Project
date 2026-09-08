import axiosClient from "./axiosClient";

const BASE = "/dashboard";

export function getDashboardSummary() {
  return axiosClient.get(`${BASE}/summary`).then((res) => res.data);
}

export function getEmployeeStats() {
  return axiosClient.get(`${BASE}/employees`).then((res) => res.data);
}

export function getDepartmentStats() {
  return axiosClient.get(`${BASE}/departments`).then((res) => res.data);
}

export function getAttendanceStatsToday() {
  return axiosClient.get(`${BASE}/attendance/today`).then((res) => res.data);
}

export function getLeaveStatsCurrentMonth() {
  return axiosClient.get(`${BASE}/leaves/current-month`).then((res) => res.data);
}

export function getPayrollStatsCurrentMonth() {
  return axiosClient.get(`${BASE}/payroll/current-month`).then((res) => res.data);
}
