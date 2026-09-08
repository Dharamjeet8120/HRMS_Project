import axiosClient from "./axiosClient";

const BASE = "/employees";

export function getAllEmployees() {
  return axiosClient.get(BASE).then((res) => res.data);
}

export function getEmployeeById(id) {
  return axiosClient.get(`${BASE}/${id}`).then((res) => res.data);
}

export function createEmployee(employee) {
  return axiosClient.post(BASE, employee).then((res) => res.data);
}

export function updateEmployee(id, employee) {
  return axiosClient.put(`${BASE}/${id}`, employee).then((res) => res.data);
}

export function deleteEmployee(id) {
  return axiosClient.delete(`${BASE}/${id}`).then((res) => res.data);
}

export function searchEmployeesByName(name) {
  return axiosClient.get(`${BASE}/search`, { params: { name } }).then((res) => res.data);
}

export function getEmployeeByCode(code) {
  return axiosClient.get(`${BASE}/code/${code}`).then((res) => res.data);
}

export function getEmployeesByDepartment(departmentId) {
  return axiosClient.get(`${BASE}/department/${departmentId}`).then((res) => res.data);
}

export function getEmployeesByDesignation(designation) {
  return axiosClient.get(`${BASE}/designation/${designation}`).then((res) => res.data);
}

export function getEmployeesByStatus(status) {
  return axiosClient.get(`${BASE}/status/${status}`).then((res) => res.data);
}

// Resolves the most specific search endpoint available given whichever
// filters are filled in, falling back to "get everything".
export async function searchEmployees({ name, departmentId, designation, status }) {
  if (name) return searchEmployeesByName(name);
  if (departmentId) return getEmployeesByDepartment(departmentId);
  if (designation) return getEmployeesByDesignation(designation);
  if (status) return getEmployeesByStatus(status);
  return getAllEmployees();
}
