// src/ThankYouPage.jsx

import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import './ThankYouPage.css';

const ThankYouPage = () => {
  const location = useLocation();
  // Access the order details passed from the MenuPage
  const orderDetails = location.state?.orderDetails;

  return (
    <div className="thank-you-container">
      <div className="thank-you-card">
        <div className="checkmark-icon">✓</div>
        <h1 className="thank-you-title">Thank You!</h1>
        <p className="thank-you-subtitle">Your order has been successfully placed.</p>
        
        {orderDetails && (
          <div className="order-confirmation">
            Order ID: <strong>#{orderDetails.orderId || '12345'}</strong>
          </div>
        )}

        <Link to="/" className="new-order-button">
          Place Another Order
        </Link>
      </div>
    </div>
  );
};

export default ThankYouPage;