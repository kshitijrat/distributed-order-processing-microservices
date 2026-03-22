**README.md:**

```markdown
# Distributed Order Processing System

A microservices-based order processing system built with Spring Boot, demonstrating service decomposition, inter-service communication via REST APIs, and JWT-based authentication.

## Architecture

The system is divided into four independent microservices and an API Gateway:

- **api-gateway** (Port 8080) - Single entry point for all client requests
- **user-service** (Port 8081) - User registration, authentication, and JWT token generation
- **order-service** (Port 8082) - Order placement and status management
- **payment-service** (Port 8083) - Payment processing with retry logic and fraud detection
- **inventory-service** (Port 8084) - Stock management and availability tracking

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security with JWT (jjwt 0.12.x)
- Spring Data JPA
- MySQL
- Spring Cloud Gateway
- Swagger / SpringDoc OpenAPI
- Maven
- Lombok

## Service Flow

1. Client registers and logs in via user-service and receives a JWT token
2. Client places an order via order-service with the JWT token
3. order-service checks stock availability with inventory-service
4. If stock is available, order is saved and payment-service is triggered
5. payment-service processes the payment with retry logic and fraud detection
6. On payment success, inventory-service reduces the stock and order status is updated to CONFIRMED
7. On payment failure, order status is updated to FAILED

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL

### Database Setup

Create the following databases in MySQL:

```sql
CREATE DATABASE user_db;
CREATE DATABASE order_db;
CREATE DATABASE payment_db;
CREATE DATABASE inventory_db;
```

### Configuration

Each service has an `application-example.properties` file. Copy it to `application.properties` and fill in the required values:

```
spring.datasource.password=your_mysql_password
jwt.secret=your_secret_key_minimum_32_characters
```

Note: Use the same `jwt.secret` across all services.

### Running the Services

Run each service in a separate terminal in the following order:

```bash
# User Service
cd user-service
mvn spring-boot:run

# Inventory Service
cd inventory-service
mvn spring-boot:run

# Payment Service
cd payment-service
mvn spring-boot:run

# Order Service
cd order-service
mvn spring-boot:run

# API Gateway
cd api-gateway
mvn spring-boot:run
```

## API Endpoints

### User Service (via Gateway: localhost:8080)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /auth/signup | Register new user | No |
| POST | /auth/login | Login and get JWT token | No |
| GET | /api/users/{id} | Get user by ID | Yes |

### Order Service (via Gateway: localhost:8080)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/orders | Place a new order | Yes |
| GET | /api/orders/{id} | Get order by ID | Yes |
| GET | /api/orders/user/{userId} | Get orders by user | Yes |
| PUT | /api/orders/{id}/status | Update order status | No |

### Payment Service (via Gateway: localhost:8080)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/payment-processor/pay | Process payment | No |
| GET | /api/payment-processor/{orderId} | Get transaction by order | No |

### Inventory Service (via Gateway: localhost:8080)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/inventory/add | Add stock | No |
| GET | /api/inventory/check | Check stock availability | No |
| PUT | /api/inventory/reduce | Reduce stock | No |
| GET | /api/inventory/{productId} | Get stock by product | No |
| GET | /api/inventory/all | Get all stock | No |

## Swagger UI

Each service exposes Swagger UI for API documentation:

- user-service: http://localhost:8081/swagger-ui.html
- order-service: http://localhost:8082/swagger-ui.html
- payment-service: http://localhost:8083/swagger-ui.html
- inventory-service: http://localhost:8084/swagger-ui.html

## Project Structure

```
distributed-order-processing-system/
├── api-gateway/
├── user-service/
├── order-service/
├── payment-service/
└── inventory-service/
```

## Key Concepts Demonstrated

- Microservices architecture with single responsibility per service
- Inter-service communication using RestTemplate
- JWT authentication and authorization
- Payment retry logic with fraud detection simulation
- Inventory management with stock validation before order placement
- Centralized routing via API Gateway
- API documentation with Swagger
```