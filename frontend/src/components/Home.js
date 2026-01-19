import React, { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../css/Home.css";

export default function Home() {
    const navigate = useNavigate();

    useEffect(() => {
        // Check if a token exists in localStorage
        const token = localStorage.getItem("token");
        if (token) {
            // If token exists, redirect to chatbot
            navigate("/chatbot");
        }
    }, [navigate]);

    return (
        <div className="home-container">
            <h1>Welcome to Event AI!</h1>
            <p>
                Discover the best events happening in Johannesburg with Event AI! If you’re new here, please click the Register button to create your account. Once logged in, Lio will guide you step by step to find your perfect event — whether it’s concerts, workshops, or community meetups. If you already have an account, click Login to get started instantly!
            </p>
            <div className="home-buttons">
                <Link to="/login"><button>Login</button></Link>
                <Link to="/register"><button className="secondary">Register</button></Link>
            </div>
        </div>
    );
}
