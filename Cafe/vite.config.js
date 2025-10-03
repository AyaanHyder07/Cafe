import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // --- ADD THIS 'server' SECTION ---
  server: {
    proxy: {
      // Any request that starts with '/api' will be forwarded
      '/api': {
        target: 'http://localhost:8080', // Your Spring Boot backend address
        changeOrigin: true,
        secure: false,      
      }
    }
  }
})