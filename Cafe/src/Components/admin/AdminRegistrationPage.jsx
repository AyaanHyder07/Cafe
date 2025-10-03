// src/components/admin/AdminRegistrationPage.jsx
import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
// You would create CSS for this page
import './AdminShared.css';

const AdminRegistrationPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('STAFF');
    const [message, setMessage] = useState('');
    const { token, role: userRole } = useAuth();

    if (userRole !== 'OWNER') {
        return <p>Access Denied. This page is for owners only.</p>;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        const response = await fetch('/api/admin/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ username, password, role })
        });
        
        const responseText = await response.text();
        setMessage(responseText);
        if (response.ok) {
            setUsername('');
            setPassword('');
        }
    };

    return (
        <div className="admin-page">
            <h1 className="admin-page-title">Register New User</h1>
            <form onSubmit={handleSubmit} className="register-form">
                {/* Form fields for username, password, and a select for role */}
                <button type="submit">Register User</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default AdminRegistrationPage;