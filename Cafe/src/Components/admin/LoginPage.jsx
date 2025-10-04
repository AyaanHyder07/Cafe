import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth(); // Gets the login function from context
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const response = await fetch('/api/admin/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
                credentials: 'include' // CRITICAL: Sends session cookie to be created/validated
            });

            if (!response.ok) {
                throw new Error('Invalid credentials');
            }

            // Backend success: The session cookie is set by the backend.
            await response.json(); 
            login(); // Update frontend state to authenticated
            navigate('/admin/dashboard');

        } catch (err) {
            setError('Login failed. Please check your username and password.');
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h1 className="login-logo">SH</h1>
                <h2>Admin Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label htmlFor="username">Username</label>
                        <input id="username" type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                    </div>
                    <div className="input-group">
                        <label htmlFor="password">Password</label>
                        <input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit" className="login-button">Login</button>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;