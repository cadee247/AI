import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
// src/index.js
import './index.css';
// Grab the root element from your public/index.html
const rootElement = document.getElementById("root");

// Create a React 18 root
const root = ReactDOM.createRoot(rootElement);

// Render your App (App.js already wraps BrowserRouter)
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);