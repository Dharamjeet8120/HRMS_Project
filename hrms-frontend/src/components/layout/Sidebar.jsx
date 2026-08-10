import { NavLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
const navItems = [
  { name: "Dashboard", path: "/dashboard" },
  { name: "Departments", path: "/departments" },
  { name: "Employees", path: "/employees" },
  { name: "Attendance", path: "/attendance" },
  { name: "Leaves", path: "/leaves" },
  { name: "Payroll", path: "/payroll" },
];

const Sidebar = () => {
  const { user } = useAuth();

  return (
    <aside className="w-64 bg-gray-900 text-white min-h-screen p-4">
      <h1 className="text-xl font-bold mb-8 px-2">HRMS</h1>
      <nav className="space-y-1">
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `block px-4 py-2 rounded-md transition ${
                isActive
                  ? "bg-blue-600 text-white"
                  : "text-gray-300 hover:bg-gray-800"
              }`
            }
          >
            {item.name}
          </NavLink>
        ))}
      </nav>

      {user && (
        <div className="absolute bottom-4 left-4 text-xs text-gray-400">
          Logged in as {user.username} ({user.roles.join(", ")})
        </div>
      )}
    </aside>
  );
};

export default Sidebar;