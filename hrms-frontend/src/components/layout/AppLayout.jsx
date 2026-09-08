import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "./Sidebar";
import Topbar from "./Topbar";

const TITLES = [
  { match: /^\/$/, title: "Dashboard", subtitle: "Today's snapshot across the organisation" },
  { match: /^\/employees/, title: "Employees", subtitle: "Personnel records and department assignment" },
  { match: /^\/departments/, title: "Departments", subtitle: "Organisational units and department heads" },
  { match: /^\/attendance/, title: "Attendance", subtitle: "Daily check-in and check-out records" },
  { match: /^\/leaves/, title: "Leave requests", subtitle: "Applications, approvals and balances" },
  { match: /^\/payroll/, title: "Payroll", subtitle: "Monthly pay runs and payslip status" },
];

function resolveTitle(pathname) {
  const found = TITLES.find((t) => t.match.test(pathname));
  return found || { title: "HRMS", subtitle: "" };
}

export default function AppLayout() {
  const location = useLocation();
  const { title, subtitle } = resolveTitle(location.pathname);

  return (
    <div className="app-shell">
      <Sidebar />
      <div className="app-main">
        <Topbar title={title} subtitle={subtitle} />
        <div className="app-content">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
