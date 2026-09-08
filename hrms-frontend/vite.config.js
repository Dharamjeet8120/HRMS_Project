import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// HRMS frontend dev server. Must run on port 5173 — the backend's
// SecurityConfig only whitelists http://localhost:5173 for CORS.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
  },
})
