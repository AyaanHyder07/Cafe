import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './MenuPage.css';

// --- Mock Data (This would be replaced by a fetch call to your backend) ---
const mockMenuItems = [
    { itemId: 101, name: 'Classic Espresso', description: 'A rich, aromatic shot of pure coffee.', price: 3.50, isAvailable: true, category: 'Coffee & Espressos' },
    { itemId: 102, name: 'Caramel Macchiato', description: 'Steamed milk, vanilla, and espresso with a caramel drizzle.', price: 5.25, isAvailable: true, category: 'Coffee & Espressos' },
    { itemId: 103, name: 'Vanilla Latte', description: 'Smooth espresso with steamed milk and a hint of vanilla.', price: 5.00, isAvailable: true, category: 'Coffee & Espressos' },
    { itemId: 104, name: 'Iced Americano', description: 'Espresso shots topped with cold water and served over ice.', price: 4.00, isAvailable: false, category: 'Coffee & Espressos' },
    { itemId: 201, name: 'Earl Grey Supreme', description: 'A timeless classic black tea with a hint of bergamot.', price: 4.00, isAvailable: true, category: 'Teas & Infusions' },
    { itemId: 301, name: 'Almond Croissant', description: 'Flaky, buttery croissant filled with a sweet almond paste.', price: 4.50, isAvailable: true, category: 'Pastries & Cakes' },
    { itemId: 401, name: 'Orange Sunrise', description: 'Freshly squeezed orange juice, simple and perfect.', price: 5.50, isAvailable: true, category: 'Fresh Juices' },
    { itemId: 501, name: 'Avocado Toast', description: 'Sourdough toast with fresh avocado, chili flakes, and lime.', price: 8.00, isAvailable: true, category: 'Savory Bites' },
];


const MenuPage = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState([]);
  const [orderNotes, setOrderNotes] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    // In a real app, you would fetch data from your Spring Boot backend here
    // Example: fetch('/api/menu/available').then(...)
    setMenuItems(mockMenuItems);
  }, []);

  const handleAddItem = (dishToAdd) => {
    setCart([...cart, { ...dishToAdd, quantity: 1 }]);
  };

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
      // If quantity is 1, remove the item from the cart
      setCart(cart.filter(item => item.itemId !== itemIdToDecrease));
    } else {
      // Otherwise, just decrease the quantity
      setCart(cart.map(item => 
        item.itemId === itemIdToDecrease 
          ? { ...item, quantity: item.quantity - 1 } 
          : item
      ));
    }
  };
  
  const handlePlaceOrder = () => {
    // This function prepares the cart data to be sent to the backend.
    // The structure matches your backend's OrderDTO.
    const orderData = {
      customerId: 1, // This would be dynamic in a real app
      tableId: 5,   // This would be dynamic in a real app
      notes: orderNotes, // The single notes field for the entire order
      orderItems: cart.map(item => ({
        menuItemId: item.itemId,
        quantity: item.quantity,
      }))
    };
    
    console.log("Placing Order with this data:", JSON.stringify(orderData, null, 2));
    
    // In a real app, you would make your API call here.
    // For this example, we'll simulate a successful response from the backend.
    const savedOrderFromBackend = { ...orderData, orderId: Date.now() % 10000 };
    
    // Navigate to the thank-you page on success, passing order details
    navigate('/thank-you', { state: { orderDetails: savedOrderFromBackend } });
    
    // Clear the state for the next order
    setCart([]);
    setOrderNotes("");
  };

  // Calculate totals on every render
  const cartTotal = cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  const totalItems = cart.reduce((total, item) => total + item.quantity, 0);

  // Frontend-only categories for display grouping
  const categories = [ 
    'Coffee & Espressos', 
    'Teas & Infusions', 
    'Pastries & Cakes',
    'Fresh Juices',
    'Savory Bites'
  ];
  
  return (
    <div className="menu-container">
      <header className="cafe-header">
        <h1 className="cafe-name">SH</h1>
        <p className="cafe-slogan">Sip, Savor, and Smile.</p>
      </header>
      
      {categories.map((category) => {
        const itemsInCategory = menuItems.filter(item => item.category === category);
        if (itemsInCategory.length === 0) return null;

        return (
          <section key={category} className="category-section">
            <h2 className="category-title">{category}</h2>
            <div className="dishes-grid">
              {itemsInCategory.map((dish) => {
                const cartItem = cart.find(item => item.itemId === dish.itemId);

                return (
                  <div key={dish.itemId} className={`dish-card ${!dish.isAvailable ? 'unavailable' : ''}`}>
                    <div className="dish-info">
                      <h3 className="dish-name">{dish.name}</h3>
                      <p className="dish-description">{dish.description}</p>
                    </div>
                    <div className="dish-details">
                      <span className="dish-price">${dish.price.toFixed(2)}</span>
                      
                      {cartItem ? (
                        <div className="quantity-control">
                          <button onClick={() => handleDecreaseQuantity(dish.itemId)} className="quantity-btn">-</button>
                          <span className="quantity-text">{cartItem.quantity}</span>
                          <button onClick={() => handleIncreaseQuantity(dish.itemId)} className="quantity-btn">+</button>
                        </div>
                      ) : (
                        <button 
                          className="add-button" 
                          onClick={() => handleAddItem(dish)}
                          disabled={!dish.isAvailable}
                        >
                          {dish.isAvailable ? 'Add' : 'Unavailable'}
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

      {cart.length > 0 && (
        <div className="order-summary-footer">
          <div className="summary-details">
            <span className="summary-item-count">{totalItems} item{totalItems > 1 ? 's' : ''}</span>
            <textarea
              className="order-notes-input"
              placeholder="Add special instructions for the kitchen..."
              value={orderNotes}
              onChange={(e) => setOrderNotes(e.target.value)}
            ></textarea>
            <span className="summary-total-footer">Total: ${cartTotal.toFixed(2)}</span>
          </div>
          <button className="place-order-button" onClick={handlePlaceOrder}>
            Place Your Order
          </button>
        </div>
      )}
    </div>
  );
};

export default MenuPage;