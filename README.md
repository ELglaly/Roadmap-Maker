# Roadmap Maker

Roadmap Maker is a Spring Boot backend that helps users create and manage personalised learning roadmaps. It combines user accounts, JWT authentication, roadmap and milestone management, progress tracking, resource recommendations, Redis response caching, and AI-assisted roadmap generation.

## Stack

- Java 23 and Spring Boot 3.4.3
- Spring Web, Spring Data JPA, Spring Security, and WebSocket
- MySQL with Liquibase migrations
- Redis for response caching
- Spring AI with an OpenAI-compatible Gemini endpoint
- Maven, Lombok, MapStruct, and JUnit 5

## Prerequisites

- JDK 23
- Maven 3.9+ (the Maven wrapper is included)
- MySQL 8+
- Redis 7+
- An AI provider key when using AI-generated roadmaps or resources

## Configuration

Copy the example environment file and replace the placeholders:

```bash
cp .env.example .env
```

The application reads these values from the environment:

| Variable | Purpose |
| --- | --- |
| `SPRING_DATASOURCE_USERNAME` | MySQL username |
| `SPRING_DATASOURCE_PASSWORD` | MySQL password |
| `SPRING_AI_GEMINI_API_KEY` | AI provider API key |
| `JWT_SECRET_KEY` | Secret used to sign JWTs |
| `REDIS_HOST` / `REDIS_PORT` | Redis connection settings |
| `REDIS_PASSWORD` | Optional Redis password |
| `SPRING_PROFILES_ACTIVE` | Spring profile, usually `dev`, `test`, or `prod` |

Create a MySQL database named `roadmapmaker`, make sure MySQL and Redis are running, and keep `.env` private. Liquibase applies the schema from `src/main/resources/db/changelog` when the application starts.

## Run and test

```bash
./mvnw test
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

The default development server runs on `http://localhost:8080`. Tests use the `test` profile and an in-memory H2 database where configured.

## API overview

The controllers currently expose endpoints for:

- `/api/v1/users` — user registration, login, logout, lookup, update, and deletion
- `/api/v1/roadmaps` — roadmap creation, retrieval, listing, search, and user roadmaps
- `/api/v1/milestone` — milestone lookup by roadmap
- `/api/progress` — progress creation, update, retrieval, and deletion
- `/api/v1/responses` — previously generated responses

Most protected endpoints require an `Authorization: Bearer <token>` header. Check the controller classes for request and response payloads because the API is still evolving.

## WebSocket

Roadmap generation progress is also exposed through the WebSocket configuration under the roadmap API path. Clients should connect with the same authentication and deployment considerations as the corresponding HTTP API.

## Project layout

```text
src/main/java/com/roadmap/backendapi/
├── controller       HTTP endpoints
├── service          application and domain services
├── entity           JPA entities and enums
├── request          request models
├── response         response models
├── mapper           DTO/entity mapping
├── security         JWT authentication
├── validator        input validation
├── exception        application exceptions and handlers
└── Config           Spring configuration
```

## Security notes

Never commit `.env`, API keys, database passwords, JWT secrets, or cloud credentials. Use a different long random JWT secret in every environment and rotate any credential that has ever been committed. Production should use the `prod` profile and should not expose actuator details publicly.

## Contributing

Keep changes focused, add tests for behavior changes, run the Maven test suite before opening a pull request, and use concise commit messages that explain the change. Do not commit build output, IDE metadata, or local configuration.

## License

No license is currently specified for this repository.
