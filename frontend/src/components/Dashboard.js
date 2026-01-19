import React from "react";
import { useNavigate } from "react-router-dom";
import "../css/Dashboard.css";

export default function Dashboard() {
    const navigate = useNavigate();

    console.log("Dashboard component rendered");

    // Navigate to the login page
    const goToLogin = () => {
        navigate("/login");
    };

    // Navigate to the register page
    const goToRegister = () => {
        navigate("/register");
    };

    return (
        <div className="dashboard-container">
            <div className="dashboard-content">
                <h1>Welcome to Event AI</h1>
                <p>Please register or login to continue.</p>
                <div style={{ display: "flex", gap: "1rem", justifyContent: "center" }}>
                    <button onClick={goToRegister}>Register</button>
                    <button onClick={goToLogin}>Login</button>
                </div>
            </div>
        </div>
    );
}
