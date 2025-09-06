# Credit Risk API 
### Project: Credit Risk API
### Package: `com.cbfacademy.creditrisk`
### Artifact: `creditrisk`
### Description: A Spring Boot RESTful API that connects to Experian’s Credit Data API, stores retrieved data in a local database, and computes credit risk metrics for analysis and decision-making.
### Data Source: Experian Credit Data API (all customer + loan information). 
------

## 📖 Overview
The **Credit Risk API** demonstrates how a financial institution could build an internal service on top of an external credit bureau (Experian).

It does three things:
1. Fetch borrower & loan data from Experian API.

2. Store data in a MySQL database for later queries and filtering.
   
3. Enrich data with credit risk metrics like:
   
   * Probability of Default (PD) → likelihood borrower might default
     
   * Loss Given Default (LGD) → estimated loss if borrower defaults
     
   * Exposure at Default (EAD) → how much money is at risk
     
   * Overall Risk Score & Risk Grade → final classification (Low, Medium, High)
------

## 🎯 Purpose
This project is designed to:
+ Show how external credit bureau APIs can be integrated into a custom risk service.
  
+ Provide credit officers and analysts with stored, queryable data instead of hitting Experian directly each time.

+ Demonstrate how to compute credit risk measures using bureau data.
------

## ✨ Features
✅ Connect to Experian API and retrieve credit data.

✅ Store borrower and loan data locally in MySQL.

✅ Compute risk metrics (PD, LGD, EAD, Risk Score).

✅ Categorize borrowers into risk grades (Low, Medium, High).

✅ Make preliminary loan decisions (Approve / Reject).

✅ Swagger UI for interactive documentation. 

------

## 🧮 Risk Assessment Logic
The RiskService works as follows:

1. Call Experian API → fetch credit history, defaults, repayment records.

2. Store results in the local database.

3. Compute:

   * PD → likelihood the borrower will default

   * LGD → estimated loss if borrower defaults

   * EAD → exposure amount at risk

   * Risk Score → combined model output

4. Assign Risk Grade + decision (Approve/Reject)

**Example:**
```Java
{
  "applicationId": 101,
  "borrowerName": "John Doe",
  "riskScore": 82,
  "riskGrade": "Low",
  "decision": "Approve",
  "PD": 0.05,
  "LGD": 0.4,
  "EAD": 10000
}
```
------

## 🧑‍🤝‍🧑 User Stories
+ Developer → “I want a `/health` endpoint so I know the service is running.”

+ Credit Officer → “I want to fetch borrower credit data from Experian and store it locally.”

+ Risk Analyst → “I want to calculate PD/LGD/EAD/RiskScore for each borrower.”

+ Manager → “I want to retrieve stored applications and track their credit risk.”
------

## 🗂 Data Flow
```sql
+------------------+       +---------------------+       +----------------+
|  Experian API    | --->  |  Credit Risk API    | --->  |   MySQL DB     |
| (Credit Data)    |       |  (Spring Boot)      |       | (Stored Data)  |
+------------------+       +---------------------+       +----------------+
                                |
                                v
                        +----------------+
                        | RiskService    |
                        | Computes PD,   |
                        | LGD, EAD,      |
                        | Risk Score     |
                        +----------------+
                                |
                                v
                        +----------------+
                        | Swagger UI     |
                        | Interactive    |
                        | API docs       |
                        +----------------+
```
+ Experian API: Provides credit data.

+ Credit Risk API: Fetches data, stores locally, computes risk metrics.

+ MySQL DB: Stores all borrower and loan info for queries.

+ Swagger UI: Allows users to test endpoints without writing code.
------
## 🛠 Tech Stack
+ **Java 17**

+ **Spring Boot 3.5**

+ **Spring Data JPA** → Database persistence

+ **MySQL** → Data storage

+ **Spring Validation** → Input validation

+ **Experian API** → External credit data

+ **Springdoc OpenAPI** → Swagger UI

+ **JUnit 5** → Unit testing
------
## 🚀 Getting Started
**Prerequisites**

+ Java 17+

+ Maven

+ MySQL

+ Experian API credentials (API key required)

**Setup**

1. Clone the repo:
```java
git clone <repository-url>
cd creditrisk
```
2. Configure database & Experian credentials in `application.properties`:
```java
spring.datasource.url=jdbc:mysql://localhost:3306/creditrisk_db
spring.datasource.username=root
spring.datasource.password=yourpassword

experian.api.url=https://api.experian.com/credit-data
experian.api.key=your_api_key_here
```
3. Run the app:
```java
mvn spring-boot:run

```
4. Open Swagger UI:
http://localhost:8080/swagger-ui.html
------
