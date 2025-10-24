# 🪶 Journal Services
### (Backend Repo: https://github.com/HBrahmbhatt/JournalServices)
### (Front End Repo: https://github.com/HBrahmbhatt/journal-web)

---

## 🖼️ Frontend Preview

![Journal App Screenshot](https://github.com/user-attachments/assets/8668129e-7e04-4d35-a192-73eb3015b812)

---

## 📘 Overview
**Journal Services** is the backend for the Journal App — a secure and scalable journaling platform designed to help users document their thoughts, emotions, and daily reflections.

Built using **Java Spring Boot** and **MongoDB**, the backend follows a **microservices architecture**, emphasizing modularity, scalability, and security.  
Each service adheres to the **Single Responsibility Principle**, ensuring cleaner, maintainable code.

---

## ⚙️ Key Features

- **🧠 Emotion Tags**: Associate emotions with journal entries for better categorization and insights.  
- **🔐 Authentication & Security**: Built with **Spring Security**, implementing JWT authentication with plans to add **OAuth** for third-party login (Google, GitHub, etc.).  
- **🧾 Logging & Monitoring**: Includes structured logging and **SonarQube** integration for continuous code quality monitoring.  
- **🧩 Microservices Architecture**: Modular service design for user management, journaling, and authentication.  
- **🧪 Testing & Reliability**: Integrated **JUnit** test cases to ensure robustness and scalability.

---

## 🧱 Technologies Used

- **Java Spring Boot 3+**  
- **MongoDB**  
- **Spring Security**  
- **Spring Data JPA (Mongo)**  
- **SonarQube**  
- **JUnit 5**  
- **OAuth 2.0 (Planned)**  

---

## 🧩 Architecture Overview

```

[ Client (React + TS + Tailwind) ]
              |
              ↓
[ API Gateway (Spring Boot) ]
              |
┌────────────────────────────┐
|  Auth Service (JWT, OAuth) |
|  Journal Service           |
|  User Service              |
└────────────────────────────┘
            |
            ↓
    [ MongoDB Atlas ]

```

---

## 🚀 API Documentation

API documentation is powered by **Springdoc OpenAPI (Swagger)**.

- **Swagger UI:**  
```

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

```
- **OpenAPI Docs (JSON):**  
```

[http://localhost:8080/api/v1/api-docs](http://localhost:8080/api/v1/api-docs)

````

---

## 🧰 Setup & Run

### 1. Clone the repository
```bash
git clone https://github.com/HBrahmbhatt/JournalServices.git
cd JournalServices
````

### 2. Configure environment variables

Before running, configure sensitive values via environment variables:

```bash
SPRING_DATA_MONGODB_URI=<your-mongodb-uri>
JWT_SECRET_KEY=<your-secret-key>
```

Or use `application.properties` with placeholders like:

```properties
spring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI}
jwt.secret=${JWT_SECRET_KEY}
```

### 3. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🌱 Future Enhancements

* **AI-Powered Summaries** — Integrate **LLM-based** summarization for weekly or monthly reflective reports.
* **Analytics Dashboard** — Emotion trends and journaling frequency charts.
* **Email & Notification Service** — Gentle reminders and writing streak updates.

---

> *“Every log, like every journal entry, tells a story of growth.”*
