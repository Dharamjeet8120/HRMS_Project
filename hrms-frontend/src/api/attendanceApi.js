import axiosClient from "./axiosClient";

const BASE = "/attendance";

export function getAllAttendance() {
  return axiosClient.get(BASE).then((res) => res.data);
}

export function getAttendanceById(id) {
  return axiosClient.get(`${BASE}/${id}`).then((res) => res.data);
}

export function markAttendance(attendance) {
  return axiosClient.post(BASE, attendance).then((res) => res.data);
}

export function updateAttendance(id, attendance) {
  return axiosClient.put(`${BASE}/${id}`, attendance).then((res) => res.data);
}

export function deleteAttendance(id) {
  return axiosClient.delete(`${BASE}/${id}`).then((res) => res.data);
}

export function getAttendanceByEmployee(employeeId) {
  return axiosClient.get(`${BASE}/employee/${employeeId}`).then((res) => res.data);
}

export function getAttendanceByEmployeeAndDate(employeeId, date) {
  return axiosClient.get(`${BASE}/employee/${employeeId}/date/${date}`).then((res) => res.data);
}

export function getAttendanceByEmployeeAndRange(employeeId, startDate, endDate) {
  return axiosClient
    .get(`${BASE}/employee/${employeeId}/range`, { params: { startDate, endDate } })
    .then((res) => res.data);
}

export function getAttendanceByDate(date) {
  return axiosClient.get(`${BASE}/date/${date}`).then((res) => res.data);
}

export function getAttendanceByDateRange(startDate, endDate) {
  return axiosClient.get(`${BASE}/range`, { params: { startDate, endDate } }).then((res) => res.data);
}

export function getAttendanceByStatus(status) {
  return axiosClient.get(`${BASE}/status/${status}`).then((res) => res.data);
}

export function checkOut(employeeId, date) {
  return axiosClient.put(`${BASE}/checkout/${employeeId}`, null, { params: { date } }).then((res) => res.data);
}

export function countPresentDays(employeeId, startDate, endDate) {
  return axiosClient
    .get(`${BASE}/employee/${employeeId}/count-present`, { params: { startDate, endDate } })
    .then((res) => res.data);
}

export async function searchAttendance({ employeeId, date, startDate, endDate, status }) {
  if (employeeId && date) return [await getAttendanceByEmployeeAndDate(employeeId, date)];
  if (employeeId && startDate && endDate) return getAttendanceByEmployeeAndRange(employeeId, startDate, endDate);
  if (employeeId) return getAttendanceByEmployee(employeeId);
  if (date) return getAttendanceByDate(date);
  if (startDate && endDate) return getAttendanceByDateRange(startDate, endDate);
  if (status) return getAttendanceByStatus(status);
  return getAllAttendance();
}
