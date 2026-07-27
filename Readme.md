# Employee Management System

A modern microservices-based Employee Management System built with Spring Boot and Spring Cloud. This project demonstrates enterprise-grade architecture patterns including service discovery, API gateway, inter-service communication, and circuit breakers.

## 📋 Overview

This is a distributed system composed of multiple independent microservices that work together to provide a complete employee management solution. The system includes employee and department management services with a centralized API gateway for unified access.

## 🏗️ Architecture

The project consists of the following microservices:

### 1. **API Gateway** (`api-gateway/`)
- Entry point for all client requests
- Routes requests to appropriate microservices
- Built with Spring Cloud Gateway
- Registers with Eureka Discovery Server
- **Port**: 8080

### 2. **Employee Service** (`employee-service/`)
- Manages employee information and operations
- Provides REST APIs for employee CRUD operations
- Communicates with Department Service via Feign Client
- Implements circuit breaker pattern with Resilience4j
- Includes Swagger/OpenAPI documentation
- **Port**: 8081

### 3. **Department Service** (`department-service/`)
- Manages department information and operations
- Provides REST APIs for department CRUD operations
- Supports employee queries related to departments
- Implements circuit breaker pattern with Resilience4j
- Includes Swagger/OpenAPI documentation
- **Port**: 8082

### 4. **Discovery Server** (`discovery-server/`)
- Service Registry using Netflix Eureka
- Enables service-to-service discovery
- Central hub for service registration
- **Port**: 8761

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.4
- **Cloud**: Spring Cloud 2025.0.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Resilience**: Resilience4j (Circuit Breaker)
- **Inter-service Communication**: OpenFeign
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Additional Tools**: Lombok, Spring Boot Actuator, Spring Data JPA

## 📦 Dependencies Overview

### Common Dependencies (All Services)
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Data JPA
- MySQL Connector/J
- Spring Cloud Starter Netflix Eureka Client
- Spring Cloud Starter OpenFeign
- Spring Cloud Starter Circuit Breaker (Resilience4j)
- SpringDoc OpenAPI Starter WebMVC UI
- Lombok
- Spring Boot Starter Actuator
- Spring Boot Starter Test

### Service-Specific Dependencies
- **API Gateway**: Spring Cloud Starter Gateway
- **Discovery Server**: Spring Cloud Starter Netflix Eureka Server

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- MySQL Server
- Git

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/raju-dongala/EmployeeManagementSystem.git
   cd EmployeeManagementSystem
