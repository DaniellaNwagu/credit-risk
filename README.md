Credit Risk API

Project: Credit Risk API
Package: com.cbfacademy.creditrisk
Artifact: creditrisk
Description: A Spring Boot RESTful API for managing borrowers and loan applications, including basic credit risk assessment and decision logic.

Table of Contents

Overview

Features

Technology Stack

Project Structure

Getting Started

API Endpoints

Risk Scoring Logic

Exception Handling

Testing

Contributing


Overview

The Credit Risk API allows financial institutions or developers to:

Manage borrower data (Create, Read, Update, Delete)

Submit loan applications and automatically calculate risk scores

Categorize loans as Low, Medium, or High risk

Make preliminary approval or rejection decisions based on risk

This API demonstrates key Java/Spring Boot concepts, including REST endpoints, JPA, MySQL integration, exception handling, and inheritance.


Features

Full CRUD operations for borrowers and loans

Filter borrowers by last name

Filter loans by risk grade

Automatic risk scoring and grading

Decision making based on risk (Approve/Reject)

Global exception handling with meaningful HTTP responses

Auditing fields (createdAt, updatedAt) for all entities


Technology Stack

Java: 17

Spring Boot: 3.x

Spring Data JPA

MySQL for database

Maven for project management

JUnit 5 for unit testing


Project Structure
<img width="833" height="227" alt="image" src="https://github.com/user-attachments/assets/c4306a76-bccc-44d5-8d33-073541771b54" />


Getting Started
Prerequisites

Java 17 or higher

Maven

MySQL server (running locally or remotely)

Setup

Clone the repository:

git clone <repository-url>
cd creditrisk

Configure MySQL connection in src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/creditrisk_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


Build and run the project:

mvn clean install
mvn spring-boot:run


The API will be available at: http://localhost:8080/api/


API Endpoints
Borrowers
<img width="1038" height="346" alt="image" src="https://github.com/user-attachments/assets/5137e6a2-50ee-4a54-bb20-3452a6040e23" />

Loans
<img width="1076" height="349" alt="image" src="https://github.com/user-attachments/assets/9bc643c5-c4aa-4dda-92f7-5fc39df8fd20" />


Risk Scoring Logic

Loan risk score:
riskScore = 100 - (loanAmount / 10,000)

Risk grade:

Low → score ≥ 75

Medium → 50 ≤ score < 75

High → score < 50

Decision:

Approve → Low or Medium risk

Reject → High risk

Exception Handling

ResourceNotFoundException → 404 Not Found

Generic exceptions → 500 Internal Server Error

Example response for missing borrower:

{
  "status": 404,
  "message": "Borrower not found with id 1"
}

Testing

Unit tests written using JUnit 5

Example: testing BorrowerService CRUD operations

Run tests:

mvn test

Contributing

Fork the repository

Create a feature branch:

git checkout -b feature/xyz


Commit your changes:

git commit -m "Add feature"


Push the branch:

git push origin feature/xyz


Create a pull request
