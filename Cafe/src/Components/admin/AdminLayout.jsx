// src/components/admin/AdminLayout.jsx
import React from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './AdminLayout.css';

const AdminLayout = () => {
    const { role, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="admin-layout">
            <aside className="admin-sidebar">
                <div className="sidebar-header">
                    <h1 className="sidebar-logo">SH</h1>
                    <span>Admin Panel</span>
                </div>
                <nav className="admin-nav">
                    <NavLink to="/admin/dashboard" className="nav-link">Dashboard</NavLink>
                    <NavLink to="/admin/availability" className="nav-link">Menu Availability</NavLink>
                    
                    {/* OWNER ONLY: Conditional rendering based on role */}
                    {role === 'OWNER' && (
                        <>
                            <hr className="nav-divider" />
                            <NavLink to="/admin/menu-management" className="nav-link owner-link">Full Menu Control</NavLink>
                            <NavLink to="/admin/register" className="nav-link owner-link">Register User</NavLink>
                        </>
                    )}
                </nav>
                <div className="sidebar-footer">
                    <button onClick={handleLogout} className="logout-button">Logout</button>
                </div>
            </aside>
            <main className="admin-main-content">
                <Outlet /> {/* This is where the specific admin pages will be rendered */}
            </main>
        </div>
    );
};

export default AdminLayout;