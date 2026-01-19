# AI

Event AI is an interactive web application that helps users discover and explore events in Johannesburg. Users can browse events in categories like Tech, Music, Cooking, and Christmas, and interact with Lio, the AI assistant, to get event details.



Features

User authentication with JWT (signup/login)

Interactive AI chatbot (Lio) for event recommendations

Categories: Tech, Music, Cooking, Christmas

View event details and dates

Full-stack architecture with React frontend and Spring Boot backend

Demo data for events (can be replaced with real database)



event-ai/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/...     # Controllers, Services, Security
│   ├── pom.xml
│   └── application.properties
├── frontend/                # React frontend
│   ├── src/
│   │   ├── components/      # Login, Register, Chatbot
│   │   └── css/             # Styles
│   ├── package.json
│   └── public/
└── README.md



Getting Started
Prerequisites

Java 17+

Maven

Node.js 18+

npm or yarn

Backend Setup

Navigate to the backend folder:

cd backend


Install dependencies and build the project:

mvn clean package


Run the backend:

java -jar target/eventai_backend-0.0.1-SNAPSHOT.jar


The backend runs on http://localhost:8080 (or PORT if using Render).

Frontend Setup

Navigate to the frontend folder:

cd frontend


Install dependencies:

npm install


Run the frontend locally:

npm start


The frontend runs on http://localhost:3000.

Environment Variables

For deployment (Render, Netlify, etc.):

Backend: PORT (Render sets this automatically)

Frontend: API URL for backend, e.g.:

const API_URL = "https://eventai-backend.onrender.com";

Deployment

Backend: Render (Java Web Service)
Build Command: mvn clean package
Start Command: java -jar target/eventai_backend-0.0.1-SNAPSHOT.jar

Frontend: Render (Static Site)
Build Command: npm install && npm run build
Publish Directory: build/

Usage

Register a new user or log in.

Chat with Lio to browse events.

Ask for more details about events, switch categories, or explore different dates.

Tech Stack

Frontend: React, Axios, React Router, CSS

Backend: Spring Boot, Java, JWT Authentication

Database: PostgreSQL (for real deployment)

Hosting: Render


