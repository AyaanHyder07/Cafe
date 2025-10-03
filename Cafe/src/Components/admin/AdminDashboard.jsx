// src/components/admin/AdminDashboard.jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import './AdminDashboard.css';

const AdminDashboard = () => {
    const [orders, setOrders] = useState([]);
    const [isLoading, setIsLoading] = useState(true); 
    const [error, setError] = useState(null); 
    const { token } = useAuth();


    useEffect(() => {
        const fetchOrders = async () => {
            setIsLoading(true); // Start loading
            setError(null);
            try {
                const response = await fetch('/api/orders', { headers: { 'Authorization': `Bearer ${token}` }});
                if (!response.ok) throw new Error('Failed to fetch orders.');
                const data = await response.json();
                setOrders(data);
            } catch (err) {
                setError(err.message); // Set error message
            } finally {
                setIsLoading(false); // Stop loading
            }
        };

        fetchOrders();
        const intervalId = setInterval(fetchOrders, 20000); // Poll every 20 seconds
        return () => clearInterval(intervalId);
    }, [token]);

    const formatDateTime = (dateTimeString) => {
        const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateTimeString).toLocaleDateString('en-US', options);
    };

    if (isLoading) return <div>Loading orders...</div>; 
    if (error) return <div>Error: {error}</div>; 

    return (
        <div className="admin-page">
            <h1 className="admin-page-title">Live Order Dashboard</h1>
            <div className="order-grid">
                {orders.length === 0 ? (
                    <p>No new orders yet.</p>
                ) : (
                    orders.map(order => (
                        <div key={order.orderId} className="order-card">
                            <div className="order-card-header">
                                <h3>Order #{order.orderId}</h3>
                                <span>Table: {order.table.tableNumber}</span>
                            </div>
                            <div className="order-card-body">
                                <p><strong>Time:</strong> {formatDateTime(order.createdAt)}</p>
                                <p><strong>Customer:</strong> {order.customer.name}</p>
                                <p><strong>Notes:</strong> {order.notes || 'None'}</p>
                                <ul>
                                    {order.orderItems.map(item => (
                                        <li key={item.orderItemId}>
                                            {item.quantity}x {item.menuItem.name}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                            <div className="order-card-footer">
                                <strong>Total: ${order.totalAmount.toFixed(2)}</strong>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default AdminDashboard;