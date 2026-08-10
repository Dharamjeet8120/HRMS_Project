import axiosInstance from "./axiosInstance";

export const getAllEmployees = () => axiosInstance.get("/employees");

export const getEmployeeById = (id) => axiosInstance.get(`/employees/${id}`);

export const createEmployee = (data) =>
  axiosInstance.post("/employees", data);

export const updateEmployee = (id, data) =>
  axiosInstance.put(`/employees/${id}`, data);

export const deleteEmployee = (id) =>
  axiosInstance.delete(`/employees/${id}`);

export const searchEmployeeByName = (name) =>
  axiosInstance.get(`/employees/search?name=${name}`);

export const getEmployeesByDepartment = (departmentId) =>
  axiosInstance.get(`/employees/department/${departmentId}`);