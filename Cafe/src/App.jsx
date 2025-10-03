// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';

// --- Import Components ---
// Public Pages
import MenuPage from './Components/MenuPage';
import ThankYouPage from './Components/ThankYouPage';

// Admin Pages
import AdminLayout from './Components/admin/AdminLayout';
import LoginPage from './Components/admin/LoginPage';
import AdminDashboard from './Components/admin/AdminDashboard';
// Assuming you create these files:
import AdminAvailabilityPage from './Components/admin/AdminAvailabilityPage';
import AdminMenuManagementPage from './Components/admin/AdminMenuManagementPage';
import AdminRegistrationPage from './Components/admin/AdminRegistrationPage';

// Utility
import ProtectedRoute from './Components/utility/ProtectedRoute';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* --- Public Routes --- */}
          <Route path="/" element={<MenuPage />} />
          <Route path="/thank-you" element={<ThankYouPage />} />
          <Route path="/login" element={<LoginPage />} />

          {/* --- Protected Admin Routes (Nested inside AdminLayout) --- */}
          <Route 
            path="/admin"
            element={
              <ProtectedRoute>
                <AdminLayout />
              </ProtectedRoute>
            }
          >
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="availability" element={<AdminAvailabilityPage />} />
            <Route path="menu-management" element={<AdminMenuManagementPage />} />
            <Route path="register" element={<AdminRegistrationPage />} />
           
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;