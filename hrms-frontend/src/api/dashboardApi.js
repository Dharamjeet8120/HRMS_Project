import axiosInstance from "./axiosInstance";

export const getAllDepartments = () => axiosInstance.get("/departments");

export const getDepartmentById = (id) =>
    axiosInstance.get(`/departments/${id}`);

export const createDepartment = (data) =>
    axiosInstance.post("/departments", data);

export const updateDepartment = (id, data) =>
    axiosInstance.put(`/departments/${id}`, data);

export const deleteDepartment = (id) =>
    axiosInstance.delete(`/departments/${id}`);

export const searchDepartmentByName = (name) =>
    axiosInstance.get(`/departments/search/name/${name}`);

export const getDashboardSummary = () =>
    axiosInstance.get("/dashboard/summary");