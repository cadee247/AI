import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/Register.css";

export default function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");

        try {
            const res = await axios.post(
                "https://ai-lthf.onrender.com/api/auth/register", // updated URL
                { username, password },
                { withCredentials: true } // send cookies if backend uses them
            );

            if (res.status === 200) {
                setMessage(`✅ Registration successful! Welcome, ${res.data.username}`);
                setUsername("");
                setPassword("");
                // Optional: redirect straight to login or chatbot
                setTimeout(() => navigate("/login"), 1000);
            }
        } catch (err) {
            console.error("Register error:", err);

            if (err.response) {
                setMessage(err.response.data?.message || "⚠️ Registration failed");
            } else {
                setMessage("⚠️ Server not responding");
            }
        }
    };

    return (
        <div className="register-container">
            <div className="register-card">
                <h2>Register</h2>

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

                    <button type="submit">Register</button>
                </form>

                {message && <p className="message">{message}</p>}

                <span className="link" onClick={() => navigate("/login")}>
                    Already have an account? Login
                </span>
            </div>
        </div>
    );
}
