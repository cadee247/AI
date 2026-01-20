import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/Login.css";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    // Auto-redirect if user is already logged in
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            navigate("/chatbot");
        }
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");

        try {
            const res = await axios.post(
                "https://ai-lthf.onrender.com/api/auth/login", // Updated backend URL
                { username, password },
                { withCredentials: true } // send cookies
            );

            // Save token if returned
            if (res.data?.token) {
                localStorage.setItem("token", res.data.token);
            }

            // Save username for welcome messages
            localStorage.setItem("username", username);

            setMessage("✅ Login successful! Redirecting...");

            setTimeout(() => {
                navigate("/chatbot"); // Redirect to chatbot
            }, 800);
        } catch (err) {
            console.error(err);

            if (err.response) {
                setMessage(`⚠️ Login failed: ${err.response.data}`);
            } else if (err.request) {
                setMessage("⚠️ Login failed: No response from server.");
            } else {
                setMessage(`⚠️ Login failed: ${err.message}`);
            }
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button type="submit">Login</button>
                </form>
                {message && <p className="message">{message}</p>}

                <p className="link" onClick={() => navigate("/register")}>
                    Don't have an account? Register
                </p>
            </div>
        </div>
    );
}
