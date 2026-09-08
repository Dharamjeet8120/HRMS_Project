import { createContext, useCallback, useMemo, useState } from "react";
import * as authApi from "../api/authApi";

export const AuthContext = createContext(null);

function readStoredUser() {
  try {
    const raw = localStorage.getItem("hrms_user");
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readStoredUser);
  const [token, setToken] = useState(() => localStorage.getItem("hrms_token"));

  const login = useCallback(async (username, password) => {
    const data = await authApi.login(username, password);
    // JwtResponseDTO: { token, username, roles }
    const nextUser = { username: data.username, roles: data.roles || [] };
    localStorage.setItem("hrms_token", data.token);
    localStorage.setItem("hrms_user", JSON.stringify(nextUser));
    setToken(data.token);
    setUser(nextUser);
    return nextUser;
  }, []);

  const register = useCallback((payload) => authApi.register(payload), []);

  const logout = useCallback(() => {
    localStorage.removeItem("hrms_token");
    localStorage.removeItem("hrms_user");
    setToken(null);
    setUser(null);
  }, []);

  const hasRole = useCallback(
    (...roles) => {
      if (!user?.roles) return false;
      return roles.some((r) => user.roles.includes(r));
    },
    [user]
  );

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(token),
      login,
      register,
      logout,
      hasRole,
      // Admins and HR staff manage records; plain employees get a
      // read-mostly view (apply/cancel leave, check in/out).
      isManager: hasRole("ROLE_ADMIN", "ROLE_HR"),
      isAdmin: hasRole("ROLE_ADMIN"),
    }),
    [user, token, login, register, logout, hasRole]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
