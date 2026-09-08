import axiosClient from "./axiosClient";

const BASE = "/departments";

export function getAllDepartments() {
  return axiosClient.get(BASE).then((res) => res.data);
}

export function getDepartmentById(id) {
  return axiosClient.get(`${BASE}/${id}`).then((res) => res.data);
}

export function createDepartment(department) {
  return axiosClient.post(BASE, department).then((res) => res.data);
}

export function updateDepartment(id, department) {
  return axiosClient.put(`${BASE}/${id}`, department).then((res) => res.data);
}

export function deleteDepartment(id) {
  return axiosClient.delete(`${BASE}/${id}`).then((res) => res.data);
}

export function searchDepartmentsByName(name) {
  return axiosClient.get(`${BASE}/search/name/${name}`).then((res) => res.data);
}

export function searchDepartmentByCode(code) {
  return axiosClient.get(`${BASE}/search/code/${code}`).then((res) => res.data);
}

export function searchDepartmentsByHead(head) {
  return axiosClient.get(`${BASE}/search/head/${head}`).then((res) => res.data);
}

export async function searchDepartments({ name, code, head }) {
  if (name) return searchDepartmentsByName(name);
  if (code) return [await searchDepartmentByCode(code)];
  if (head) return searchDepartmentsByHead(head);
  return getAllDepartments();
}
