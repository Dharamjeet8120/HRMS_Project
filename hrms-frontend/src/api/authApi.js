import axiosClient from "./axiosClient";

// POST /api/v1/auth/login  -> JwtResponseDTO { token, username, roles }
export function login(username, password) {
  return axiosClient.post("/auth/login", { username, password }).then((res) => res.data);
}

// POST /api/v1/auth/register -> plain text confirmation message
export function register({ username, password, email, roles, employeeId }) {
  return axiosClient
    .post("/auth/register", { username, password, email, roles, employeeId: employeeId || null })
    .then((res) => res.data);
}
