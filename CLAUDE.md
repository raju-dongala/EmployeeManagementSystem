# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Build all services:
```
./mvnw clean package
```

Build specific service (from its directory):
```
./mvnw clean package
```

Run tests (all):
```
./mvnw test
```

Run single test:
```
./mvnw test -Dtest=EmployeeServiceApplicationTests
./mvnw test -Dtest=DepartmentServiceApplicationTests
```

Run application (from service directory):
```
./mvnw spring-boot:run
```

Docker (all services):
```
docker-compose up --build
```

Stop Docker:
```
docker-compose down
```

## Architecture

Microservices system with 4 Spring Boot services communicating via Eureka discovery and Feign clients.

**Services:**
- **discovery-server** (port 8761) - Eureka server for service registration/discovery
- **employee-service** (port 8082) - Employee CRUD operations, Feign client to department-service
- **department-service** (port 8081) - Department CRUD operations
- **api-gateway** (port 8060) - Spring Cloud Gateway routing to services via Eureka

**Service communication:**
- Employee-service calls Department-service via OpenFeign (`DepartmentClient`)
- Circuit breaker (Resilience4j) on `getEmployeeWithDepartment()` with fallback
- Gateway routes: `/api/v1/employees/**` → employee-service, `/api/v1/departments/**` → department-service

**Database:**
- MySQL containerized via docker-compose
- employee-service: `employee_db` (employees table)
- department-service: `department_db` (departments table)

**Profiles:**
- `application.properties` - localhost config
- `application-docker.properties` - Docker network config
- `application-aws.properties` - AWS deployment config

**Entity relationships:**
- Employee has `departmentCode` field (string, not FK)
- Department identified by unique `departmentCode`
- `GET /api/v1/employees/{id}/department` returns combined employee + department data

**Common packages:**
- `controller` - REST endpoints with `@RestController`
- `service` - Service interfaces, `Impl` - implementations
- `repository` - Spring Data JPA repositories
- `entity` - JPA entities with Lombok
- `dto/request` - Request DTOs
- `dto/response` - Response DTOs
- `exception` - Custom exceptions + `GlobalExceptionHandler`
- `feign` - Feign client interfaces (employee-service only)
- `mapper` - Entity ↔ DTO mapping utilities

**Stack:**
- Java 17, Spring Boot 3.5.4, Spring Cloud 2025.0.0
- MySQL 8.0, Lombok, OpenAPI/Swagger, Resilience4j circuit breaker