import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import './MenuPage.css';

const MenuPage = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState([]);
  const [orderNotes, setOrderNotes] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [customerName, setCustomerName] = useState("");
  const [customerPhone, setCustomerPhone] = useState("");
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const tableId = searchParams.get('tableId');

  useEffect(() => {
    if (tableId) {
        console.log("Customer is at Table:", tableId);
    } else {
        console.error("No table ID found in URL! The QR code might be invalid.");
    }

    const fetchMenuItems = async () => {
      try {
        const response = await fetch('/api/menu/available');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setMenuItems(data);
      } catch (error) {
        console.error("Failed to fetch menu items:", error);
      }
    };
    fetchMenuItems();
  }, [tableId]);

  const handleAddItem = (dishToAdd) => setCart([...cart, { ...dishToAdd, quantity: 1 }]);

  const handleIncreaseQuantity = (itemIdToIncrease) => {
    setCart(cart.map(item => 
      item.itemId === itemIdToIncrease 
        ? { ...item, quantity: item.quantity + 1 } 
        : item
    ));
  };
  
  const handleDecreaseQuantity = (itemIdToDecrease) => {
    const itemInCart = cart.find(item => item.itemId === itemIdToDecrease);
    if (itemInCart.quantity === 1) {
      setCart(cart.filter(item => item.itemId !== itemIdToDecrease));
    } else {
      setCart(cart.map(item => 
        item.itemId === itemIdToDecrease 
          ? { ...item, quantity: item.quantity - 1 } 
          : item
      ));
    }
  };
  
  const handleOpenModal = () => {
    if (!tableId) {
        alert("Cannot place order: No table specified. Please scan a valid QR code.");
        return;
    }
    setIsModalOpen(true);
  };
  const handleCloseModal = () => setIsModalOpen(false);

  const handleConfirmOrder = async () => {
    if (!customerName || !customerPhone) {
        alert("Please enter your name and phone number.");
        return;
    }
    
    const orderData = {
      customerId: 1,
      tableId: parseInt(tableId),
      notes: orderNotes,
      orderItems: cart.map(item => ({
        menuItemId: item.itemId,
        quantity: item.quantity,
      }))
    };
    
    try {
        const response = await fetch('/api/orders/place', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(orderData)
        });
        if (!response.ok) {
            throw new Error('Failed to place order');
        }
        const savedOrder = await response.json();
        
        navigate('/thank-you', { state: { orderDetails: savedOrder } });
        setCart([]);
        setOrderNotes("");
    } catch (error) {
        console.error("Order placement failed:", error);
        alert("Sorry, there was a problem placing your order.");
    } finally {
        handleCloseModal();
    }
  };

  const cartTotal = cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  const totalItems = cart.reduce((total, item) => total + item.quantity, 0);
  const categories = [...new Set(menuItems.map(item => item.category))];
  
 return (
    <>
      <div className="menu-container">
        <header className="cafe-header">
          <h1 className="cafe-name">SH</h1>
          <p className="cafe-slogan">Sip, Savor, and Smile.</p>
        </header>

        {categories.map((category) => {
          const itemsInCategory = menuItems.filter(
            (item) => item.category === category
          );
          if (itemsInCategory.length === 0) return null;

          return (
            <section key={category} className="category-section">
              <h2 className="category-title">{category}</h2>
              <div className="dishes-grid">
                {itemsInCategory.map((dish) => {
                  const cartItem = cart.find(
                    (item) => item.itemId === dish.itemId
                  );

                  return (
                    <div
                      key={dish.itemId}
                      className={`dish-card ${
                        !dish.isAvailable ? "unavailable" : ""
                      }`}
                    >
                      <div className="dish-info">
                        <h3 className="dish-name">{dish.name}</h3>
                        <p className="dish-description">{dish.description}</p>
                      </div>
                      <div className="dish-details">
                        <span className="dish-price">
                          ${dish.price.toFixed(2)}
                        </span>

                        {cartItem ? (
                          <div className="quantity-control">
                            <button
                              onClick={() =>
                                handleDecreaseQuantity(dish.itemId)
                              }
                              className="quantity-btn"
                            >
                              -
                            </button>
                            <span className="quantity-text">
                              {cartItem.quantity}
                            </span>
                            <button
                              onClick={() =>
                                handleIncreaseQuantity(dish.itemId)
                              }
                              className="quantity-btn"
                            >
                              +
                            </button>
                          </div>
                        ) : (
                          <button
                            className="add-button"
                            onClick={() => handleAddItem(dish)}
                            disabled={!dish.isAvailable}
                          >
                            {dish.isAvailable ? "Add" : "Unavailable"}
                          </button>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>
            </section>
          );
        })}
      </div>

      {cart.length > 0 && (
        <div className="order-summary-footer">
          <div className="summary-details">
            <span className="summary-item-count">
              {totalItems} item{totalItems > 1 ? "s" : ""}
            </span>
            <textarea
              className="order-notes-input"
              placeholder="Add special instructions for the kitchen..."
              value={orderNotes}
              onChange={(e) => setOrderNotes(e.target.value)}
            ></textarea>
            <span className="summary-total-footer">
              Total: ${cartTotal.toFixed(2)}
            </span>
          </div>
          <button className="place-order-button" onClick={handleOpenModal}>
            Place Your Order
          </button>
        </div>
      )}

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Confirm Your Order</h2>
            <p>Please provide your details to complete the order.</p>
            <input
              type="text"
              placeholder="Your Name"
              className="modal-input"
              value={customerName}
              onChange={(e) => setCustomerName(e.target.value)}
            />
            <input
              type="tel"
              placeholder="Your Phone Number"
              className="modal-input"
              value={customerPhone}
              onChange={(e) => setCustomerPhone(e.target.value)}
            />
            <div className="modal-actions">
              <button
                className="modal-btn-secondary"
                onClick={handleCloseModal}
              >
                Cancel
              </button>
              <button
                className="modal-btn-primary"
                onClick={handleConfirmOrder}
              >
                Confirm & Pay
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default MenuPage;