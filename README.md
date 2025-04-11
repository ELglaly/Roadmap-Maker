# 🗺️ Roadmap Generator

## 📌 Project Information

**Project Name:** Roadmap Generator

**Project Description:**  
An intelligent and modular **Roadmap Generator** built with **Spring Boot**, designed to create personalized learning or project paths based on user preferences, experience levels, and target goals. It supports admin and user roles, custom roadmap creation, progress tracking, and integrates AI suggestions for roadmap improvements.

## 🚀 Features

- 🔐 **User Authentication & Authorization** – Secure login/registration with JWT-based role management.
- 🧠 **AI-Enhanced Recommendations** – Smart suggestions to optimize learning or project paths.
- 📄 **Roadmap Templates** – Predefined templates for common goals (e.g., Web Development, ML, etc.).
- 📊 **Progress Tracking** – Visual progress updates per user roadmap.
- 🛠️ **Custom Roadmaps** – Users can build and modify their own step-by-step plans.
- 📨 **Notifications** – Optional reminders and updates via email.
- 🖥️ **RESTful API** – Designed for easy integration with front-end apps.
- 🧪 **Testing** – Comprehensive unit and integration testing using JUnit 5 and Mockito.

## 🛠️ Technologies & Tools

**Languages:**  
- Java

**Frameworks & Libraries:**  
- Spring Boot, Spring Security, Spring Data JPA, JavaMailSender

**Database & Storage:**  
- PostgreSQL (for production), H2 Database (for testing)

**Other:**  
- JWT, JavaMail, Lombok, MapStruct

## 🏗️ Project Structure

📂 src
 ├── 📂 main
 │   ├── 📂 java
 │   │   └── 📂 com.example.roadmapgenerator
 │   │       ├── 📂 config          # Application configuration (e.g., beans, security config)
 │   │       ├── 📂 controller      # REST API endpoints
 │   │       ├── 📂 dto             # Data Transfer Objects
 │   │       ├── 📂 entity          # JPA Entities / Models
 │   │       ├── 📂 env             # Environment-specific configuration
 │   │       ├── 📂 exception       # Custom exceptions
 │   │       ├── 📂 handler         # Global exception handling
 │   │       ├── 📂 mapper          # Mappers (e.g., using MapStruct)
 │   │       ├── 📂 repository      # Spring Data JPA repositories
 │   │       ├── 📂 request         # Request payloads
 │   │       ├── 📂 response        # Response payloads
 │   │       ├── 📂 security        # JWT filters, token utils, user auth
 │   │       ├── 📂 service         # Business logic services
 │   │       ├── 📂 utils           # Utility classes and helpers
 │   │       └── 📄 RoadmapGeneratorApplication.java  # Main entry point
 │   ├── 📂 resources
 │   │   ├── 📄 application.properties  
 ├── 📄 .gitignore
 ├── 📄 pom.xml
 └── 📄 README.md
