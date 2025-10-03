// src/context/AuthContext.js

import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState(() => {
        const token = localStorage.getItem('jwt_token');
        const role = localStorage.getItem('user_role');
        if (token && role) {
            return { token, role, isAuthenticated: true };
        }
        return { token: null, role: null, isAuthenticated: false };
    });

    const login = (token, role) => {
        localStorage.setItem('jwt_token', token);
        localStorage.setItem('user_role', role);
        setAuth({ token, role, isAuthenticated: true });
    };

    const logout = () => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_role');
        setAuth({ token: null, role: null, isAuthenticated: false });
    };

    return (
        <AuthContext.Provider value={{ ...auth, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};