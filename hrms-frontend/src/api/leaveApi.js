import axiosInstance from "./axiosInstance";

export const applyLeave = (data) => axiosInstance.post("/leaves", data);

export const getAllLeaves = () => axiosInstance.get("/leaves");

export const getLeavesByEmployee = (employeeId) =>
  axiosInstance.get(`/leaves/employee/${employeeId}`);

export const getLeavesByStatus = (status) =>
  axiosInstance.get(`/leaves/status/${status}`);

export const approveLeave = (id, approvedBy) =>
  axiosInstance.put(`/leaves/${id}/approve?approvedBy=${approvedBy}`);

export const rejectLeave = (id, approvedBy, rejectionReason) =>
  axiosInstance.put(
    `/leaves/${id}/reject?approvedBy=${approvedBy}&rejectionReason=${rejectionReason}`
  );

export const cancelLeave = (id) =>
  axiosInstance.put(`/leaves/${id}/cancel`);

export const deleteLeave = (id) => axiosInstance.delete(`/leaves/${id}`);