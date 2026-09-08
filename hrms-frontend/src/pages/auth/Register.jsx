import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import { extractErrorMessage } from "../../api/axiosClient";
import { ROLE_OPTIONS } from "../../utils/constants";

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: "",
    password: "",
    email: "",
    roles: ["ROLE_EMPLOYEE"],
    employeeId: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [submitting, setSubmitting] = useState(false);

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
  }

  function toggleRole(role) {
    setForm((f) => {
      const has = f.roles.includes(role);
      const roles = has ? f.roles.filter((r) => r !== role) : [...f.roles, role];
      return { ...f, roles };
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (form.roles.length === 0) {
      setError("Select at least one role.");
      return;
    }

    setSubmitting(true);
    try {
      await register({
        username: form.username,
        password: form.password,
        email: form.email,
        roles: form.roles,
        employeeId: form.employeeId ? Number(form.employeeId) : null,
      });
      setSuccess("Account created. You can sign in now.");
      setTimeout(() => navigate("/login"), 1200);
    } catch (err) {
      setError(extractErrorMessage(err, "Could not create the account."));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <div className="auth-mark">Create account</div>
        <div className="auth-mark-sub">Register a new HRMS user</div>

        {error && <div className="state-banner error">{error}</div>}
        {success && <div className="state-banner info">{success}</div>}

        <form onSubmit={handleSubmit}>
          <div className="auth-field">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              minLength={4}
              value={form.username}
              onChange={(e) => update("username", e.target.value)}
              required
            />
          </div>
          <div className="auth-field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              value={form.email}
              onChange={(e) => update("email", e.target.value)}
              required
            />
          </div>
          <div className="auth-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              minLength={6}
              value={form.password}
              onChange={(e) => update("password", e.target.value)}
              required
            />
          </div>
          <div className="auth-field">
            <label htmlFor="employeeId">Linked employee ID (optional)</label>
            <input
              id="employeeId"
              type="number"
              min="1"
              value={form.employeeId}
              onChange={(e) => update("employeeId", e.target.value)}
              placeholder="Leave blank if none"
            />
          </div>
          <div className="auth-field">
            <label>Roles</label>
            <div className="role-check-row">
              {ROLE_OPTIONS.map((role) => (
                <label className="role-check" key={role}>
                  <input
                    type="checkbox"
                    checked={form.roles.includes(role)}
                    onChange={() => toggleRole(role)}
                  />
                  {role.replace("ROLE_", "")}
                </label>
              ))}
            </div>
          </div>
          <button className="btn btn-primary auth-submit" type="submit" disabled={submitting}>
            {submitting ? "Creating…" : "Create account"}
          </button>
        </form>

        <div className="auth-foot">
          Already have an account? <Link to="/login">Sign in</Link>
        </div>
      </div>
    </div>
  );
}
