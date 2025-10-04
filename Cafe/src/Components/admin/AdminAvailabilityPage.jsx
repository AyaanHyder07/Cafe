import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import './AdminShared.css';

const AdminAvailabilityPage = () => {
    const [menuItems, setMenuItems] = useState([]);
    const { isAuthenticated } = useAuth();

    useEffect(() => {
        const fetchAllItems = async () => {
            if (!isAuthenticated) return;
            const response = await fetch('/api/menu/all', {
                credentials: 'include' // Sends the session cookie
            });
            const data = await response.json();
            setMenuItems(data);
        };
        fetchAllItems();
    }, [isAuthenticated]);

    const handleToggle = async (itemId) => {
        const response = await fetch(`/api/menu/${itemId}/availability`, {
            method: 'PUT',
            credentials: 'include' // Sends the session cookie
        });
        if (response.ok) {
            const updatedItem = await response.json();
            setMenuItems(currentItems => 
                currentItems.map(item => item.itemId === updatedItem.itemId ? updatedItem : item)
            );
        }
    };

    return (
        <div className="admin-page">
            <h1 className="admin-page-title">Menu Availability</h1>
            <div className="availability-grid">
                {menuItems.map(item => (
                    <div key={item.itemId} className={`availability-card ${!item.isAvailable ? 'unavailable' : ''}`}>
                        <div className="card-info">
                            <h3 className="card-name">{item.name}</h3>
                            <p className="card-category">{item.category}</p>
                        </div>
                        <label className="toggle-switch">
                            <input
                                type="checkbox"
                                checked={item.isAvailable}
                                onChange={() => handleToggle(item.itemId)}
                            />
                            <span className="slider"></span>
                        </label>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AdminAvailabilityPage;