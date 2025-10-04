import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import './AdminShared.css';
import './AdminMenuManagementPage.css';

const AdminMenuManagementPage = () => {
    const [menuItems, setMenuItems] = useState([]);
    const { isAuthenticated } = useAuth(); // Now only checking if a user is logged in

    // --- State for the Modal Form ---
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newItem, setNewItem] = useState({
        name: '',
        description: '',
        price: '',
        category: '',
        isAvailable: true
    });

    // Fetch all menu items when the component loads
    useEffect(() => {
        const fetchAllItems = async () => {
            if (!isAuthenticated) return;
            try {
                const response = await fetch('/api/menu/all', { credentials: 'include' });
                if (!response.ok) {
                    throw new Error('Could not fetch menu items.');
                }
                const data = await response.json();
                setMenuItems(data);
            } catch (error) {
                console.error("Failed to fetch menu items:", error);
            }
        };

        fetchAllItems();
    }, [isAuthenticated]); // Rerun fetch when auth status changes

    // --- Handler Functions for the Modal ---
    const handleOpenModal = () => setIsModalOpen(true);
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setNewItem({ name: '', description: '', price: '', category: '', isAvailable: true });
    };

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        setNewItem(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmitNewItem = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/menu', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({
                    ...newItem,
                    price: parseFloat(newItem.price)
                })
            });

            if (!response.ok) {
                throw new Error('Failed to add new item.');
            }

            const addedItem = await response.json();
            setMenuItems(currentItems => [...currentItems, addedItem]);
            handleCloseModal();

        } catch (error) {
            console.error("Error submitting new item:", error);
            alert("Error: Could not add new item.");
        }
    };
    
    const handleDelete = async (itemId) => {
        if (window.confirm('Are you sure you want to delete this item?')) {
            try {
                const response = await fetch(`/api/menu/${itemId}`, {
                    method: 'DELETE',
                    credentials: 'include'
                });

                if (!response.ok) {
                    throw new Error('Failed to delete item.');
                }
                
                setMenuItems(currentItems => currentItems.filter(item => item.itemId !== itemId));

            } catch (error) {
                console.error("Error deleting item:", error);
                alert("Error: Could not delete item.");
            }
        }
    };

    // The access check is REMOVED, all logged-in admins see this page
    
    return (
        <div className="admin-page">
            <h1 className="admin-page-title">Full Menu Management</h1>
            <button className="add-new-btn" onClick={handleOpenModal}>Add New Item</button>
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
                            <td>₹{item.price.toFixed(2)}</td>
                            <td>
                                <button className="edit-btn">Edit</button>
                                <button onClick={() => handleDelete(item.itemId)} className="delete-btn">Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* --- The Modal Form for Adding a New Item --- */}
            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Add New Menu Item</h2>
                        <form onSubmit={handleSubmitNewItem}>
                            <input name="name" value={newItem.name} onChange={handleInputChange} placeholder="Dish Name" required className="modal-input" />
                            <textarea name="description" value={newItem.description} onChange={handleInputChange} placeholder="Description" required className="modal-input"></textarea>
                            <input name="price" value={newItem.price} onChange={handleInputChange} type="number" step="0.01" placeholder="Price (e.g., 250.00)" required className="modal-input" />
                            <input name="category" value={newItem.category} onChange={handleInputChange} placeholder="Category" required className="modal-input" />
                            <div className="modal-checkbox">
                                <label htmlFor="isAvailable">Available for order?</label>
                                <input id="isAvailable" name="isAvailable" type="checkbox" checked={newItem.isAvailable} onChange={handleInputChange} />
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="modal-btn-secondary" onClick={handleCloseModal}>Cancel</button>
                                <button type="submit" className="modal-btn-primary">Save Item</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminMenuManagementPage;