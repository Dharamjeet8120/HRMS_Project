import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

export default function Topbar({ title, subtitle }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const initials = user?.username ? user.username.slice(0, 2).toUpperCase() : "?";
  const primaryRole = user?.roles?.[0]?.replace("ROLE_", "") || "USER";

  function handleLogout() {
    logout();
    navigate("/login", { replace: true });
  }

  return (
    <header className="topbar">
      <div>
        <div className="topbar-title">{title}</div>
        {subtitle && <div className="topbar-sub">{subtitle}</div>}
      </div>
      <div className="topbar-user">
        <div className="user-chip">
          <span className="user-chip-name">{user?.username}</span>
          <span className="user-chip-role">{primaryRole}</span>
        </div>
        <div className="avatar-badge">{initials}</div>
        <button className="btn btn-ghost btn-sm" onClick={handleLogout}>
          Sign out
        </button>
      </div>
    </header>
  );
}
