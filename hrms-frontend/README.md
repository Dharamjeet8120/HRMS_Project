# HRMS Frontend

A React (Vite) frontend for the [HRMS_Project](https://github.com/Dharamjeet8120/HRMS_Project) Spring Boot backend — employees, departments, attendance, leave requests and payroll, behind JWT login.

## Stack

- React 18 + React Router 6
- Axios for API calls (with a JWT-attaching interceptor)
- Plain CSS design system (`src/index.css`) — no UI framework
- No TypeScript, no state-management library — `AuthContext` + local component state is enough for this size of app

## Getting started

1. **Start the backend first.** It must be running on `http://localhost:8080` with a MySQL database configured (see the backend's `application.properties`). The backend's CORS config only allows `http://localhost:5173`, so keep the frontend on that port (the included `vite.config.js` already pins it).

2. **Install dependencies:**
   ```
   npm install
   ```

3. **Configure the API URL** (only needed if your backend isn't on the default host/port):
   ```
   cp .env.example .env
   ```
   `.env` sets `VITE_API_BASE_URL` — defaults to `http://localhost:8080/api/v1`.

4. **Run the dev server:**
   ```
   npm run dev
   ```
   Visit `http://localhost:5173`.

5. **Create your first user.** The backend has no seeded users. Open `/register`, create an account with the `ROLE_ADMIN` role (and optionally `ROLE_HR`), then sign in. `ROLE_ADMIN` / `ROLE_HR` accounts can manage everything; `ROLE_EMPLOYEE` accounts get a read-mostly view (they can still apply for/cancel their own leave and mark attendance).

## Project structure

```
src/
  api/            one file per backend controller (axios calls, no UI logic)
  context/        AuthContext — JWT + user + roles, stored in localStorage
  hooks/          useAuth()
  components/
    layout/       Sidebar, Topbar, AppLayout (the shell around every page)
    common/       DataTable, StatusBadge, Modal-ish dialogs, ProtectedRoute
    dashboard/    StatBlock
  pages/          one folder per module, mirroring the sidebar
  utils/          enum lists, status→badge-colour mapping, date/currency formatting
```

Each `api/*Api.js` file maps 1:1 to a backend `*Controller`. Where a controller exposes several narrow search endpoints (e.g. `EmployeeController` has `/search`, `/code/{code}`, `/department/{id}`, `/designation/{d}`, `/status/{s}`), the matching API file also exports a `searchX(filters)` helper that picks the most specific endpoint for whatever filters are filled in on the page, falling back to "get all".

## Notes on the backend contract

- **Auth:** `POST /auth/login` returns `{ token, username, roles }`. The token is sent as `Authorization: Bearer <token>` on every request via an axios interceptor. A `401` response clears the stored session and redirects to `/login`.
- **Dates/times:** `LocalDate` fields use `<input type="date">` (`yyyy-MM-dd`, matches Jackson's default). `LocalTime` fields use `<input type="time">` (`HH:mm`), which Jackson's default `ISO_LOCAL_TIME` parser accepts fine.
- **Enums:** Status/type enums (employee status, leave type, payroll status, etc.) are hardcoded in `utils/constants.js` to match the `enum` definitions in the Java entities. If you add a new enum value on the backend, add it there too.
- **Role gating is client-side only.** The backend's `SecurityConfig` currently only requires `.authenticated()` on these endpoints (no `@PreAuthorize`), so hiding "Add/Edit/Delete" buttons from `ROLE_EMPLOYEE` here is a UI convenience, not real enforcement. If you want that enforced, add method-level security on the backend.

## Design

A "personnel ledger" look — ink navy sidebar, brass accent, a slab serif for headings/numbers, hairline-bordered tables instead of card grids. All tokens live at the top of `src/index.css` (`:root`) if you want to reskin it.
