import axiosInstance from "./axiosInstance";

export const markAttendance = (data) =>
  axiosInstance.post("/attendance", data);

export const getAllAttendance = () => axiosInstance.get("/attendance");

export const getAttendanceByEmployee = (employeeId) =>
  axiosInstance.get(`/attendance/employee/${employeeId}`);

export const getAttendanceByDate = (date) =>
  axiosInstance.get(`/attendance/date/${date}`);

export const updateAttendance = (id, data) =>
  axiosInstance.put(`/attendance/${id}`, data);

export const deleteAttendance = (id) =>
  axiosInstance.delete(`/attendance/${id}`);

export const checkOut = (employeeId, date) =>
  axiosInstance.put(`/attendance/checkout/${employeeId}?date=${date}`);