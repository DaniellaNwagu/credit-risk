README.md
# Credit Risk API – Detailed Documentation

## Project Overview

The Credit Risk API is a Spring Boot RESTful service designed to manage borrowers and loan applications while performing basic credit risk assessment. The primary goal of this project is to demonstrate how an organization can internally manage credit risk data, calculate preliminary risk metrics, and make data-driven loan decisions without relying on external systems for each request.

This project simulates a realistic credit risk workflow and provides a foundation for more advanced implementations, such as integration with external credit bureaus. 

## Objectives

**Borrower and Loan Management:**
Provide CRUD operations for borrowers and loans using RESTful endpoints.

**Risk Assessment:**
Automatically calculate a Risk Score, Risk Grade, and Loan Decision for each loan application based on loan amount relative to a reference value.

**Data Storage:**
Store all borrower and loan information in a local MySQL database for persistence and querying.

**Professional API Design:**
Implement DTOs, global exception handling, and layered architecture for maintainability and separation of concerns.

**Testing and Validation:**
Provide unit tests to ensure correctness and consistency of the API.

**Secure Database Configuration:**
Allow users to configure database credentials in a local application.properties file to prevent committing passwords to source control and reduce the risk of data leaks.


## Core Features

### 1. Borrower Management

**Endpoints:** `/api/borrowers`

**Features:** Create, read, update, delete operations.

**Validation Rules:**
- Annual income must be positive.
- Date of birth must be provided.


### 2. Loan Application Management

**Endpoints:** `/api/loans`

**Features:**
- Each loan application is linked to a borrower via a many-to-one JPA relationship.
- Automatic risk assessment:
  - **Risk Score:** 100 - (loanAmount / 10000) (clamped 0–100)
  - **Risk Grade:** Low (≥75), Medium (≥50), High (<50)
  - **Decision:** Reject if High; Approve otherwise


### 3. Global Exception Handling

Implemented via GlobalExceptionHandler with ResourceNotFoundException.

Returns meaningful HTTP status codes:
- 404 → Resource not found
- 500 → Unexpected errors


### 4. Database Persistence

**Database:** MySQL

**BaseEntity:** Provides id, createdAt, updatedAt for auditing.

**Inheritance:** Borrower and LoanApplication extend BaseEntity.


**Configuration:**
Create a local application.properties file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/creditrisk_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```
To load the sample data:

```mysql -u root -p creditrisk_db < src/main/resources/data.sql```


This prevents committing passwords to source control.

**Sample Data:**
The application includes sample data in `data.sql` with 5 borrowers and 6 loan applications for testing the API endpoints.


### 5. Unit Testing

**Unit Tests Added:**
- **LoanApplicationTest** (8 tests) - Tests `LoanApplication` model class
- **BorrowerServiceTest** (9 tests) - Tests `BorrowerService` class with mocked repository
- **LoanApplicationServiceTest** (14 tests) - Tests `LoanApplicationService` class including risk scoring logic



## API Examples

### 1. Create Loan Application

```http
POST /api/loans
Content-Type: application/json

{
  "borrowerId": 2,
  "loanAmount": 20000.0,
  "termMonths": 24,
  "loanType": "Personal"
}
```

**Response:**

```json
{
  "id": 1,
  "borrowerId": 2,
  "loanAmount": 20000.0,
  "termMonths": 24,
  "loanType": "Personal",
  "riskScore": 98.0,
  "riskGrade": "Low",
  "decision": "Approve"
}
```

### 2. Retrieve Loan by ID

```http
GET /api/loans/1
```

**Response:**

```json
{
  "id": 1,
  "borrowerId": 2,
  "loanAmount": 20000.0,
  "termMonths": 24,
  "loanType": "Personal",
  "riskScore": 98.0,
  "riskGrade": "Low",
  "decision": "Approve"
}
```
## Architecture

The Credit Risk API follows a layered architecture:

- *Model*: JPA entities (Borrower, LoanApplication) with BaseEntity inheritance
- *Repository*: Spring Data JPA for database operations
- *Service*: Business logic and risk assessment calculations
- *Controller*: REST endpoints handling HTTP requests

## Getting Started

### 1. Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+

### 2. Clone Repository

```bash
git clone <repository-url>
cd creditrisk
```

### 3. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE creditrisk_db;
```

Create a local application.properties file (do not commit):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/creditrisk_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```


### 4. Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The service runs by default at: http://localhost:8080


### 5. Testing the API

**Example flows:**
1. Create a borrower
2. Submit a loan application
3. Retrieve loan information and verify risk metrics

### 6. Unit Testing

**Run all tests:**

```bash
mvn test
```

All 31 unit tests run without database connection using Mockito for mocking repository dependencies.

## Technologies Used

- **Spring Boot 3.x**: Framework
- **Spring Data JPA**: Database access
- **MySQL**: Database
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework
- **Maven**: Build tool