import { Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/common/ProtectedRoute";
import AppLayout from "./components/layout/AppLayout";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Dashboard from "./pages/Dashboard";

import EmployeeList from "./pages/employees/EmployeeList";
import EmployeeForm from "./pages/employees/EmployeeForm";

import DepartmentList from "./pages/departments/DepartmentList";
import DepartmentForm from "./pages/departments/DepartmentForm";

import AttendanceList from "./pages/attendance/AttendanceList";
import MarkAttendance from "./pages/attendance/MarkAttendance";

import LeaveList from "./pages/leaves/LeaveList";
import ApplyLeave from "./pages/leaves/ApplyLeave";

import PayrollList from "./pages/payroll/PayrollList";
import GeneratePayroll from "./pages/payroll/GeneratePayroll";

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route
          path="/"
          element={
            <ProtectedRoute>
              <AppLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Dashboard />} />

          <Route path="employees" element={<EmployeeList />} />
          <Route
            path="employees/new"
            element={
              <ProtectedRoute roles={["ROLE_ADMIN", "ROLE_HR"]}>
                <EmployeeForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="employees/:id/edit"
            element={
              <ProtectedRoute roles={["ROLE_ADMIN", "ROLE_HR"]}>
                <EmployeeForm />
              </ProtectedRoute>
            }
          />

          <Route path="departments" element={<DepartmentList />} />
          <Route
            path="departments/new"
            element={
              <ProtectedRoute roles={["ROLE_ADMIN", "ROLE_HR"]}>
                <DepartmentForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="departments/:id/edit"
            element={
              <ProtectedRoute roles={["ROLE_ADMIN", "ROLE_HR"]}>
                <DepartmentForm />
              </ProtectedRoute>
            }
          />

          <Route path="attendance" element={<AttendanceList />} />
          <Route path="attendance/new" element={<MarkAttendance />} />
          <Route
            path="attendance/:id/edit"
            element={
              <ProtectedRoute roles={["ROLE_ADMIN", "ROLE_HR"]}>
                <MarkAttendance />
              </ProtectedRoute>
            }
          />

          <Route path="leaves" element={<LeaveList />} />
          <Route path="leaves/new" element={<ApplyLeave />} />

          <Route path="payroll" element={<PayrollList />} />
          <Route
            path="payroll/new"
            element={
              <ProtectedRoute roles={["ROLE_ADMIN", "ROLE_HR"]}>
                <GeneratePayroll />
              </ProtectedRoute>
            }
          />
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
