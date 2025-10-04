import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import "./AdminShared.css";

const AdminRegistrationPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("ADMIN"); // Simplified default role
  const [message, setMessage] = useState("");
  
  // All role checks have been removed from this component

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    // ... logic to call /api/admin/register
  };

  return (
    <div className="admin-page">
      <h1 className="admin-page-title">Register New User</h1>
      <form onSubmit={handleSubmit} className="register-form">
        {/* Form fields for username, password, and a select for role */}
        <div className="input-group">
          <label htmlFor="new-username">Username</label>
          <input
            id="new-username"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="New Username"
            required
          />
        </div>
        <div className="input-group">
          <label htmlFor="new-password">Password</label>
          <input
            id="new-password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="New Password"
            required
          />
        </div>
        <div className="input-group">
          <label htmlFor="new-role">Role</label>
          <select
            id="new-role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
          >
            <option value="ADMIN">ADMIN</option>
          </select>
        </div>
        <button type="submit">Register User</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
};

export default AdminRegistrationPage;