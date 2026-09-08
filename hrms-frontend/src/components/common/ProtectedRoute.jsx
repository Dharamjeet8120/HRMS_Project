import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

// Wrap any route that requires a logged-in session. Optionally pass
// `roles` to also require one of those roles (e.g. ["ROLE_ADMIN"]).
export default function ProtectedRoute({ children, roles }) {
  const { isAuthenticated, hasRole } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (roles && roles.length > 0 && !hasRole(...roles)) {
    return (
      <div className="app-content">
        <div className="state-banner error">
          You don't have permission to view this page. Ask an admin or HR user for access.
        </div>
      </div>
    );
  }

  return children;
}
