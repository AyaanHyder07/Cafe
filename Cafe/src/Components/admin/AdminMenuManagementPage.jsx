// src/components/admin/AdminMenuManagementPage.jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
// You would create CSS for this page and a separate Modal component
import './AdminShared.css';

const AdminMenuManagementPage = () => {
    const [menuItems, setMenuItems] = useState([]);
    const { token, role } = useAuth();

    // This check is a second layer of defense, but the main security is on the backend and router
    if (role !== 'OWNER') {
        return <p>Access Denied. This page is for owners only.</p>;
    }

    useEffect(() => {
        // Fetch logic similar to Availability page...
    }, [token]);

    const handleDelete = async (itemId) => {
        if (window.confirm('Are you sure you want to delete this item?')) {
            await fetch(`/api/menu/${itemId}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            // Re-fetch or filter items after delete...
        }
    };

    return (
        <div className="admin-page">
            <h1 className="admin-page-title">Full Menu Management</h1>
            <button className="add-new-btn">Add New Item</button>
            <table className="menu-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {menuItems.map(item => (
                        <tr key={item.itemId}>
                            <td>{item.name}</td>
                            <td>{item.category}</td>
                            <td>${item.price.toFixed(2)}</td>
                            <td>
                                <button className="edit-btn">Edit</button>
                                <button onClick={() => handleDelete(item.itemId)} className="delete-btn">Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdminMenuManagementPage;