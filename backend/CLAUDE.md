# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a Spring Boot 3.5.5 application using Java 21 with Gradle as the build tool. The project uses JWT-based authentication with secure cookies.

## Directory Structure
```
src/main/java/com/act/ldk/
├── common/
│   ├── config/         # Configuration classes (SecurityConfig, etc.)
│   ├── security/       # Security components (JWT, filters, authentication)
│   └── utils/          # Utility classes
├── controller/         # REST API controllers
├── domain/
│   ├── entity/         # JPA entities
│   └── repository/     # JPA repositories
├── dto/                # Data Transfer Objects
└── service/            # Business logic services
```

## Build and Run Commands

### Build the project
```bash
./gradlew build
```

### Run the application
```bash
./gradlew bootRun
```

### Run tests
```bash
./gradlew test
```

### Run a specific test class
```bash
./gradlew test --tests com.act.ldk.ActApplicationTests
```

### Run a specific test method
```bash
./gradlew test --tests "com.act.ldk.ActApplicationTests.contextLoads"
```

### Clean and rebuild
```bash
./gradlew clean build
```

### Check dependencies
```bash
./gradlew dependencies
```

## Architecture and Structure

### Core Technologies
- **Spring Boot 3.5.5**: Main framework for building the application
- **Spring Data JPA**: For database persistence layer
- **Spring Security**: For authentication and authorization
- **Spring Web**: For building REST APIs and web services
- **JWT (jjwt)**: For token-based authentication
- **Lombok**: For reducing boilerplate code (annotations for getters, setters, constructors)
- **H2 Database**: In-memory database for development
- **JUnit 5**: For unit and integration testing

### Security Architecture
- JWT tokens are stored in secure, HttpOnly cookies
- Token validation occurs on each request via JwtAuthenticationFilter
- BCrypt is used for password encoding
- Stateless session management

### Key Configuration Notes
- Java 21 is required (configured via toolchain in build.gradle)
- The application name is "act" (as configured in application.yml)
- JWT secret and expiration are configured in application.yml
- H2 console is enabled for development