# 🗺️ AI-Powered Learning Roadmap Generator

## 📌 Project Information

**Project Name:** AI-Powered Learning Roadmap Generator

**Project Description:**  
An intelligent and modular **Learning Roadmap Generator** built with **Spring Boot** and integrated with **AI services**. The system creates personalized learning paths based on user preferences, experience levels, and target goals using AI-driven recommendations. It features real-time progress tracking via WebSocket, Redis caching for improved performance, and comprehensive resource management for each milestone.

## 🚀 Features

- 🤖 **AI-Enhanced Learning Paths** – Intelligent roadmap generation using Spring AI and Google Cloud AI Platform
- 🔐 **User Authentication & Authorization** – Secure JWT-based authentication with role management
- 📄 **Roadmap Templates** – Predefined templates for common learning goals
- 📊 **Real-time Progress Tracking** – Live progress updates via WebSocket integration
- 🛠️ **Custom Roadmaps** – User-created and modifiable learning paths
- 📨 **Smart Notifications** – Automated reminders and milestone updates
- 🖥️ **RESTful API** – Comprehensive API for frontend integration
- 🎯 **Resource Management** – AI-curated learning resources for each milestone
- 🔄 **Redis Caching** – Performance optimization for API responses
- 🧪 **Testing** – Extensive unit and integration testing

## 🛠️ Technologies & Tools

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

## ⚙️ Prerequisites

- Java 23 or higher
- Maven 3.9.9 or higher
- MySQL Server
- Redis Server 7.4.2
- Google Cloud AI Platform API key
- Spring AI OpenAI integration key

## 📥 Installation

### 1. Install Java 23
```bash
# macOS (using Homebrew)
brew install openjdk@23

# Ubuntu/Debian
sudo apt-get update
sudo apt-get install openjdk-23-jdk

# Windows
# Download and install from https://adoptium.net/
```

### 2. Install Maven
```bash
# macOS
brew install maven

# Ubuntu/Debian
sudo apt-get install maven

# Windows
# Download and install from https://maven.apache.org/
```

### 3. Install Redis
```bash
# macOS
brew install redis

# Ubuntu/Debian
sudo apt-get install redis-server

# Windows
# Download and install from https://redis.io/
```

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
git clone <repository-url>
cd roadmap-generator

# Install dependencies
mvn clean install

# Start the application
mvn spring-boot:run
```

## 🚀 Quick Start

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/v1/users/register \
-H "Content-Type: application/json" \
-d '{
  "username": "user123",
  "password": "password123",
  "email": "user@example.com",
  "goal": "Learn Java Programming",
  "interests": "Software Development",
  "skills": "Basic Programming"
}'
```

### 2. Generate a Roadmap
```bash
curl -X POST http://localhost:8080/api/v1/roadmaps/create/{userId} \
-H "Authorization: Bearer your_jwt_token"
```

### 3. Track Progress
```bash
curl -X POST http://localhost:8080/api/progress \
-H "Authorization: Bearer your_jwt_token" \
-H "Content-Type: application/json" \
-d '{
  "userId": 1,
  "milestoneId": 1,
  "completedAt": "2024-03-21T10:00:00Z"
}'
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

## 🔒 Security

The application uses JWT-based authentication with the following security features:
- Token-based authentication
- Role-based access control
- Rate limiting
- Secure headers
- CSRF protection

## 📚 API Documentation

Full API documentation is available at `http://localhost:8080/swagger-ui.html` when running the application.

## 🤝 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📝 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

