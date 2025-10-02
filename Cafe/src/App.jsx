// src/App.js - This should be correct

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MenuPage from './Components/MenuPage';
import ThankYouPage from './Components/ThankYouPage';

function App() {
  return (
    <Router>  {/* This provides the navigation context */}
      <Routes>
        <Route path="/" element={<MenuPage />} /> {/* MenuPage is INSIDE the Router */}
        <Route path="/thank-you" element={<ThankYouPage />} />
      </Routes>
    </Router>
  );
}

export default App;