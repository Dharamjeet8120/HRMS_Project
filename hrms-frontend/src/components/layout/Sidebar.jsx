import { NavLink } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

const NAV_ITEMS = [
  { to: "/", label: "Dashboard", icon: "◧", end: true },
  { to: "/employees", label: "Employees", icon: "☰" },
  { to: "/departments", label: "Departments", icon: "▤" },
  { to: "/attendance", label: "Attendance", icon: "✓" },
  { to: "/leaves", label: "Leave requests", icon: "✎" },
  { to: "/payroll", label: "Payroll", icon: "$" },
];

export default function Sidebar() {
  const { isManager } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-brand-mark">HRMS Ledger</div>
        <div className="sidebar-brand-sub">Personnel &amp; payroll system</div>
      </div>
      <nav className="sidebar-nav">
        {NAV_ITEMS.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.end}
            className={({ isActive }) => "sidebar-link" + (isActive ? " active" : "")}
          >
            <span className="sidebar-icon">{item.icon}</span>
            {item.label}
          </NavLink>
        ))}
      </nav>
      <div className="sidebar-foot">
        {isManager ? "Admin / HR access" : "Employee access"}
        <br />
        HRMS · v1.0
      </div>
    </aside>
  );
}
