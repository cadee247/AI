import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/Chatbot.css";

export default function Chatbot() {
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const [isTyping, setIsTyping] = useState(false);
    const [username, setUsername] = useState(""); // for personalized greeting
    const messagesEndRef = useRef(null);

    // ✅ Check for JWT and set username
    useEffect(() => {
        const token = localStorage.getItem("token");
        const storedUsername = localStorage.getItem("username");

        if (!token || !storedUsername) {
            navigate("/login"); // redirect to login if not logged in
        } else {
            setUsername(storedUsername);
            setMessages([
                {
                    sender: "ai",
                    text: `Hello, ${storedUsername}! 👋 I’m Lio. Tell me what you’re in the mood for — Tech, Music, Cooking, or Christmas 🎉`
                }
            ]);
        }
    }, [navigate]);

    // Auto-scroll to bottom when new messages arrive
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages, isTyping]);

    const sendMessage = async (e) => {
        e.preventDefault();
        const trimmedInput = input.trim();
        if (!trimmedInput) return;

        // Add user's message
        setMessages(prev => [...prev, { sender: "user", text: trimmedInput }]);
        setInput("");
        setIsTyping(true);

        setTimeout(async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.post(
                    "http://localhost:8080/api/ai/respond",
                    { message: trimmedInput },
                    { headers: { Authorization: `Bearer ${token}` } } // send JWT
                );

                const data = res.data;
                const aiMessages = [];

                // EVENTS RESPONSE
                if (data.type === "events" && Array.isArray(data.messages)) {
                    data.messages.forEach(msg => {
                        aiMessages.push({ sender: msg.sender, text: msg.text });
                    });
                }

                // GREETING / ERROR RESPONSE
                else if (data.reply) {
                    data.reply
                        .split("\n")
                        .filter(Boolean)
                        .forEach(line =>
                            aiMessages.push({ sender: "ai", text: line })
                        );
                }

                setMessages(prev => [...prev, ...aiMessages]);
            } catch (err) {
                setMessages(prev => [
                    ...prev,
                    { sender: "ai", text: "⚠️ Something went wrong. Try again in a bit." }
                ]);
            } finally {
                setIsTyping(false);
            }
        }, 500);
    };

    return (
        <div className="chatbot-container">
            <div className="chatbot-wrapper">
                <div className="chatbot-messages">
                    {messages.map((msg, i) => (
                        <div key={i} className={`message ${msg.sender}`}>
                            {msg.text}
                        </div>
                    ))}

                    {isTyping && (
                        <div className="message ai">Lio is typing…</div>
                    )}

                    <div ref={messagesEndRef} />
                </div>

                <form className="chatbot-input" onSubmit={sendMessage}>
                    <input
                        type="text"
                        placeholder="Tech? Music? Something chill?"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                    />
                    <button type="submit">Send</button>
                </form>
            </div>
        </div>
    );
}
