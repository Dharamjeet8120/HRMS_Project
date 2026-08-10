import axiosInstance from "./axiosInstance";

export const generatePayroll = (data) =>
  axiosInstance.post("/payroll/generate", data);

export const getAllPayrolls = () => axiosInstance.get("/payroll");

export const getPayrollsByEmployee = (employeeId) =>
  axiosInstance.get(`/payroll/employee/${employeeId}`);

export const markAsPaid = (id) =>
  axiosInstance.put(`/payroll/${id}/mark-paid`);

export const holdPayroll = (id, remarks) =>
  axiosInstance.put(`/payroll/${id}/hold?remarks=${remarks}`);

export const deletePayroll = (id) => axiosInstance.delete(`/payroll/${id}`);