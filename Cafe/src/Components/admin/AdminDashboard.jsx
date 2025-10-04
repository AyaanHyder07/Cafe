import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import './AdminDashboard.css';

const AdminDashboard = () => {
    const [orders, setOrders] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const { isAuthenticated } = useAuth();

    useEffect(() => {
        const fetchOrders = async () => {
            if (!isAuthenticated) return;
            setError(null);
            try {
                const response = await fetch('/api/orders', {
                    credentials: 'include' // Correct way to send session cookie
                });
                if (!response.ok) {
                    throw new Error('Failed to fetch orders. You may not have permission.');
                }
                const data = await response.json();
                setOrders(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOrders();
        // Optional: Polling to refresh orders
        const intervalId = setInterval(fetchOrders, 30000);
        return () => clearInterval(intervalId);
    }, [isAuthenticated]);

    // ... (rest of the component JSX is unchanged)


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
                                <strong>Total: ₹{order.totalAmount.toFixed(2)}</strong>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default AdminDashboard;