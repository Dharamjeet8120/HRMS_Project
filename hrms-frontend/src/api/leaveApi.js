import axiosClient from "./axiosClient";

const BASE = "/leaves";

export function getAllLeaves() {
  return axiosClient.get(BASE).then((res) => res.data);
}

export function getLeaveById(id) {
  return axiosClient.get(`${BASE}/${id}`).then((res) => res.data);
}

export function applyLeave(leave) {
  return axiosClient.post(BASE, leave).then((res) => res.data);
}

export function getLeavesByEmployee(employeeId) {
  return axiosClient.get(`${BASE}/employee/${employeeId}`).then((res) => res.data);
}

export function getLeavesByEmployeeAndStatus(employeeId, status) {
  return axiosClient.get(`${BASE}/employee/${employeeId}/status/${status}`).then((res) => res.data);
}

export function getLeavesByStatus(status) {
  return axiosClient.get(`${BASE}/status/${status}`).then((res) => res.data);
}

export function approveLeave(id, approvedBy) {
  return axiosClient.put(`${BASE}/${id}/approve`, null, { params: { approvedBy } }).then((res) => res.data);
}

export function rejectLeave(id, approvedBy, rejectionReason) {
  return axiosClient
    .put(`${BASE}/${id}/reject`, null, { params: { approvedBy, rejectionReason } })
    .then((res) => res.data);
}

export function cancelLeave(id) {
  return axiosClient.put(`${BASE}/${id}/cancel`).then((res) => res.data);
}

export function deleteLeave(id) {
  return axiosClient.delete(`${BASE}/${id}`).then((res) => res.data);
}

export async function searchLeaves({ employeeId, status }) {
  if (employeeId && status) return getLeavesByEmployeeAndStatus(employeeId, status);
  if (employeeId) return getLeavesByEmployee(employeeId);
  if (status) return getLeavesByStatus(status);
  return getAllLeaves();
}
