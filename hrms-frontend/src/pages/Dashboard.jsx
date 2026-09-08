import { useEffect, useState } from "react";
import { getDashboardSummary } from "../api/dashboardApi";
import { extractErrorMessage } from "../api/axiosClient";
import StatBlock from "../components/dashboard/StatBlock";
import Loader from "../components/common/Loader";
import { formatCurrency } from "../utils/format";

export default function Dashboard() {
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    getDashboardSummary()
      .then((data) => {
        if (!cancelled) setSummary(data);
      })
      .catch((err) => {
        if (!cancelled) setError(extractErrorMessage(err, "Could not load the dashboard."));
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, []);

  if (loading) return <Loader label="Loading dashboard…" />;
  if (error) return <div className="state-banner error">{error}</div>;
  if (!summary) return null;

  const { employeeStats, departmentStats, attendanceStats, leaveStats, payrollStats, employeesPerDepartment } =
    summary;

  const maxDeptCount = Math.max(1, ...(employeesPerDepartment || []).map((d) => d.employeeCount));

  return (
    <div>
      <div className="stat-row">
        <StatBlock value={employeeStats.totalEmployees} label="Total employees" />
        <StatBlock value={employeeStats.activeEmployees} label="Active" />
        <StatBlock value={employeeStats.onLeaveEmployees} label="On leave" />
        <StatBlock value={employeeStats.inactiveEmployees} label="Inactive" />
      </div>

      <div className="stat-row">
        <StatBlock value={attendanceStats.presentToday} label="Present today" />
        <StatBlock value={attendanceStats.absentToday} label="Absent today" />
        <StatBlock value={attendanceStats.onLeaveToday} label="On leave today" />
        <StatBlock value={`${attendanceStats.todayAttendancePercentage.toFixed(0)}%`} label="Attendance rate" />
      </div>

      <div className="dashboard-grid">
        <div className="panel">
          <h3>Employees per department</h3>
          <div className="panel-sub">
            {departmentStats.totalDepartments} departments · {departmentStats.activeDepartments} active ·{" "}
            {departmentStats.departmentsWithoutHead} without a head
          </div>
          {(employeesPerDepartment || []).length === 0 ? (
            <p style={{ color: "var(--color-slate)", fontSize: 13.3 }}>No departments yet.</p>
          ) : (
            employeesPerDepartment.map((d) => (
              <div className="dept-count-row" key={d.departmentId}>
                <span>{d.departmentName}</span>
                <div className="dept-count-bar">
                  <div
                    className="dept-count-bar-fill"
                    style={{ width: `${(d.employeeCount / maxDeptCount) * 100}%` }}
                  />
                </div>
                <span>{d.employeeCount}</span>
              </div>
            ))
          )}
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 18 }}>
          <div className="panel">
            <h3>Leave requests this month</h3>
            <div className="panel-sub">{leaveStats.totalRequestsThisMonth} total requests</div>
            <div className="dept-count-row">
              <span>Pending</span>
              <strong>{leaveStats.pendingRequests}</strong>
            </div>
            <div className="dept-count-row">
              <span>Approved</span>
              <strong>{leaveStats.approvedThisMonth}</strong>
            </div>
            <div className="dept-count-row">
              <span>Rejected</span>
              <strong>{leaveStats.rejectedThisMonth}</strong>
            </div>
          </div>

          <div className="panel">
            <h3>Payroll this month</h3>
            <div className="panel-sub">{payrollStats.totalPayrollsThisMonth} pay runs generated</div>
            <div className="dept-count-row">
              <span>Paid</span>
              <strong>{payrollStats.paidThisMonth}</strong>
            </div>
            <div className="dept-count-row">
              <span>On hold</span>
              <strong>{payrollStats.onHoldThisMonth}</strong>
            </div>
            <div className="dept-count-row">
              <span>Net salary paid</span>
              <strong>{formatCurrency(payrollStats.totalNetSalaryPaidThisMonth)}</strong>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
