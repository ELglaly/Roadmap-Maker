#  AI-Powered Learning Roadmap Generator

##  Project Information

**Project Name:** AI-Powered Learning Roadmap Generator

**Project Description:**  
An intelligent and modular **Learning Roadmap Generator** built with **Spring Boot** and integrated with **AI services**. The system creates personalized learning paths based on user preferences, experience levels, and target goals using AI-driven recommendations. It features real-time progress tracking via WebSocket, Redis caching for improved performance, and comprehensive resource management for each milestone.

##  Features

-  **AI-Enhanced Learning Paths** – Intelligent roadmap generation using Spring AI and Google Cloud AI Platform
-  **User Authentication & Authorization** – Secure JWT-based authentication with role management
-  **Roadmap Templates** – Predefined templates for common learning goals
-  **Real-time Progress Tracking** – Live progress updates via WebSocket integration
-  **Custom Roadmaps** – User-created and modifiable learning paths
-  **Smart Notifications** – Automated reminders and milestone updates
-  **RESTful API** – Comprehensive API for frontend integration
-  **Resource Management** – AI-curated learning resources for each milestone
-  **Redis Caching** – Performance optimization for API responses
-  **Testing** – Extensive unit and integration testing

##  Technologies & Tools

**Core:**
- Java 23
- Spring Boot 3.4.3
- Spring Security with JWT
- Spring WebSocket
- Spring Data JPA

**AI Integration:**
- Spring AI OpenAI
- Google Cloud AI Platform

**Database & Caching:**
- MySQL
- Redis 7.4.2

**Other:**
- Maven 3.9.9
- Lombok
- MapStruct
- JUnit 5


### 4. Configure Environment
Create a `.env` file in the project root:
```properties
SPRING_AI_GEMINI_API_KEY=your_api_key
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
```

### 5. Build and Run
```bash
# Clone the repository
git clone https://github.com/ELglaly/Roadmap-Maker
cd roadmap-generator

# Install dependencies
mvn clean install

# Start the application
mvn spring-boot:run
```

##  Quick Start

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/v1/users/register \
-H "Content-Type: application/json" \
-d '{
  "username": "user123",
  "password": "Password123$#",
  "email": "user@gmail.com",
  "goal": "Learn Java Programming",
  "interests": "Software Development",
  "skills": "Java Programming"
}'
```

### 2. Generate a Roadmap
```bash
curl -X POST http://localhost:8080/api/v1/roadmaps/create/{userId} \
-H "Authorization: Bearer your_jwt_token"
```


## 📁 Project Structure

```
📂 src
 ┣ 📂 main
 ┃ ┣ 📂 java
 ┃ ┃ ┗ 📂 com
 ┃ ┃   ┗ 📂 roadmap
 ┃ ┃     ┗ 📂 backendapi
 ┃ ┃       ┣ 📂 config
 ┃ ┃       ┣ 📂 controller
 ┃ ┃       ┣ 📂 dto
 ┃ ┃       ┣ 📂 entity
 ┃ ┃       ┣ 📂 env
 ┃ ┃       ┣ 📂 exception
 ┃ ┃       ┣ 📂 handler
 ┃ ┃       ┣ 📂 interceptor
 ┃ ┃       ┣ 📂 mapper
 ┃ ┃       ┣ 📂 repository
 ┃ ┃       ┣ 📂 request
 ┃ ┃       ┣ 📂 response
 ┃ ┃       ┣ 📂 security
 ┃ ┃       ┣ 📂 service
 ┃ ┃       ┣ 📂 utils
 ┃ ┃       ┃ ┗ 📂 validator
 ┃ ┃       ┃   ┗ 📂 user
 ┃ ┃       ┣ 📂 consts
 ┃ ┃       ┗ 📄 BackendapiApplication.java
 ┃ ┗ 📂 resources
 ┃   ┗ 📄 application.properties
 ┣ 📂 test
 ┃ ┗ 📂 com
 ┃   ┗ 📂 roadmap
 ┃     ┗ 📂 backendapi
 ┃       ┣ 📂 controller
 ┃       ┣ 📂 interceptor
 ┃       ┣ 📂 service
 ┃       ┃ ┣ 📂 milestone
 ┃       ┃ ┣ 📂 progress
 ┃       ┃ ┣ 📂 response
 ┃       ┃ ┣ 📂 roadmap
 ┃       ┃ ┗ 📂 user
 ┃       ┗ 📄 BackendapiApplicationTests.java
📂 target
📄 .env
📄 .gitattributes
📄 .gitignore
📄 backendapi.iml
📄 devfile.yaml
📄 HELP.md
📄 mvnw
📄 mvnw.cmd
📄 pom.xml
📄 qodana.yaml
```

##  Security

The application uses JWT-based authentication with the following security features:
- Token-based authentication
- Role-based access control
- Rate limiting
- Secure headers
- CSRF protection
