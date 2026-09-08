import axiosClient from "./axiosClient";

const BASE = "/payroll";

export function getAllPayrolls() {
  return axiosClient.get(BASE).then((res) => res.data);
}

export function getPayrollById(id) {
  return axiosClient.get(`${BASE}/${id}`).then((res) => res.data);
}

export function generatePayroll(request) {
  return axiosClient.post(`${BASE}/generate`, request).then((res) => res.data);
}

export function getPayrollsByEmployee(employeeId) {
  return axiosClient.get(`${BASE}/employee/${employeeId}`).then((res) => res.data);
}

export function getPayrollByEmployeeAndMonth(employeeId, payMonth, payYear) {
  return axiosClient.get(`${BASE}/employee/${employeeId}/month/${payMonth}/year/${payYear}`).then((res) => res.data);
}

export function getPayrollsByMonthAndYear(payMonth, payYear) {
  return axiosClient.get(`${BASE}/month/${payMonth}/year/${payYear}`).then((res) => res.data);
}

export function getPayrollsByYear(payYear) {
  return axiosClient.get(`${BASE}/year/${payYear}`).then((res) => res.data);
}

export function getPayrollsByStatus(status) {
  return axiosClient.get(`${BASE}/status/${status}`).then((res) => res.data);
}

export function markAsPaid(id) {
  return axiosClient.put(`${BASE}/${id}/mark-paid`).then((res) => res.data);
}

export function holdPayroll(id, remarks) {
  return axiosClient.put(`${BASE}/${id}/hold`, null, { params: { remarks } }).then((res) => res.data);
}

export function deletePayroll(id) {
  return axiosClient.delete(`${BASE}/${id}`).then((res) => res.data);
}

export async function searchPayrolls({ employeeId, month, year, status }) {
  if (employeeId && month && year) return [await getPayrollByEmployeeAndMonth(employeeId, month, year)];
  if (employeeId) return getPayrollsByEmployee(employeeId);
  if (month && year) return getPayrollsByMonthAndYear(month, year);
  if (year) return getPayrollsByYear(year);
  if (status) return getPayrollsByStatus(status);
  return getAllPayrolls();
}
