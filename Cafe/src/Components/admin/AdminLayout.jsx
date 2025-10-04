import React from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import "./AdminLayout.css";

const AdminLayout = () => {
  const { logout } = useAuth(); // 'role' is removed from here
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="sidebar-header">
          <h1 className="sidebar-logo">SH</h1>
          <span>Admin Panel</span>
        </div>
        <nav className="admin-nav">
          {/* All links are now always visible */}
          <NavLink to="/admin/dashboard" className="nav-link">
            Dashboard
          </NavLink>
          <NavLink to="/admin/availability" className="nav-link">
            Menu Availability
          </NavLink>
          <hr className="nav-divider" />
          <NavLink to="/admin/menu-management" className="nav-link">
            Full Menu Control
          </NavLink>
          <NavLink to="/admin/register" className="nav-link">
            Register User
          </NavLink>
        </nav>
        <div className="sidebar-footer">
          <button onClick={handleLogout} className="logout-button">
            Logout
          </button>
        </div>
      </aside>
      <main className="admin-main-content">
        <Outlet />{" "}
      </main>
    </div>
  );
};

export default AdminLayout;