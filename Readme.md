# Employee Management System

A production-ready RESTful API built with **Spring Boot** for managing employee records. Demonstrates layered architecture, Spring Data JPA, MySQL/TiDB integration, Bean Validation, and global exception handling.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.x |
| API | Spring Web (REST) |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL / TiDB Cloud |
| Build Tool | Maven |
| Utilities | Lombok, SLF4J |
| Testing | Postman |

---

## Project Structure

```
employee-management/
├── src/main/java/com/example/employeemanagement/
│   ├── EmployeeManagementApplication.java
│   ├── controller/
│   │   └── EmployeeController.java       # REST endpoints
│   ├── service/
│   │   ├── EmployeeService.java          # Service interface
│   │   └── impl/
│   │       └── EmployeeServiceImpl.java  # Business logic
│   ├── repository/
│   │   └── EmployeeRepository.java       # JPA queries
│   ├── entity/
│   │   └── Employee.java                 # JPA entity
│   ├── dto/
│   │   ├── EmployeeRequestDto.java       # Validated request body
│   │   └── EmployeeResponseDto.java      # API response shape
│   ├── mapper/
│   │   └── EmployeeMapper.java           # Entity <-> DTO conversion
│   ├── exception/
│   │   ├── ResourceNotFoundException.java
│   │   ├── GlobalExceptionHandler.java   # @RestControllerAdvice
│   │   └── ErrorResponse.java
│   └── config/
│       └── AppConfig.java
└── src/main/resources/
    ├── application.properties            # Safe to commit — no secrets
    └── application-local.properties      # Gitignored — real credentials here
```

---

## Architecture

```
Client (Postman / Browser)
        │  HTTP Request
        ▼
  EmployeeController        @RestController — routes requests, validates input
        │  DTO
        ▼
  EmployeeService           Interface — defines business contract
        │
  EmployeeServiceImpl       @Service — business logic, @Transactional
        │  Entity
        ▼
  EmployeeRepository        JpaRepository — CRUD + custom JPQL queries
        │  JPA / Hibernate
        ▼
  MySQL / TiDB Database
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+ or TiDB Cloud account
- IntelliJ IDEA (recommended)

### 1. Clone the repository

```bash
git clone https://github.com/your-username/employee-management.git
cd employee-management
```

### 2. Set up the database

**For MySQL (local):**
```sql
CREATE DATABASE IF NOT EXISTS employee_db;
```

**For TiDB Cloud:**
1. Create a free cluster at [tidbcloud.com](https://tidbcloud.com)
2. Open SQL Editor and run:
```sql
CREATE DATABASE IF NOT EXISTS employee_db;
```
Spring JPA will auto-create the `employees` table on first run.

### 3. Configure credentials

Copy the example properties file:
```bash
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
```

Fill in your real values in `application-local.properties`:

```properties
# MySQL (local)
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# TiDB Cloud (production)
# spring.datasource.url=jdbc:mysql://your-tidb-host:4000/employee_db?sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3
# spring.datasource.username=your_tidb_username.root
# spring.datasource.password=your_tidb_password
# spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

> `application-local.properties` is gitignored — your credentials are never committed.

### 4. Run the application

```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn clean package -DskipTests
java -jar target/employee-management-0.0.1-SNAPSHOT.jar
```

The API will be live at: `http://localhost:4000`

---

## API Reference

Base URL: `/api/v1/employees`

### Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/api/v1/employees` | Create employee | `201 Created` |
| `GET` | `/api/v1/employees` | Get all employees | `200 OK` |
| `GET` | `/api/v1/employees/{id}` | Get employee by ID | `200 OK` |
| `PUT` | `/api/v1/employees/{id}` | Update employee | `200 OK` |
| `DELETE` | `/api/v1/employees/{id}` | Delete employee | `204 No Content` |
| `GET` | `/api/v1/employees/department/{dept}` | Filter by department | `200 OK` |
| `GET` | `/api/v1/employees/salary-range?min=&max=` | Filter by salary range | `200 OK` |
| `GET` | `/api/v1/employees/search?name=` | Search by name | `200 OK` |

### Request Body (POST / PUT)

```json
{
  "firstName": "Rahul",
  "lastName": "Sharma",
  "email": "rahul.sharma@company.com",
  "department": "Engineering",
  "salary": 85000.00,
  "joiningDate": "2024-01-15"
}
```

### Response Body

```json
{
  "id": 1,
  "firstName": "Rahul",
  "lastName": "Sharma",
  "email": "rahul.sharma@company.com",
  "department": "Engineering",
  "salary": 85000.00,
  "joiningDate": "2024-01-15",
  "fullName": "Rahul Sharma"
}
```

### Error Response

```json
{
  "status": 404,
  "message": "Employee not found with id: 99",
  "timestamp": "2024-01-15T10:30:00",
  "validationErrors": null
}
```

### Validation Error Response

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "validationErrors": {
    "email": "Invalid email format",
    "salary": "Salary must be positive"
  }
}
```

---

## Sample Postman Requests

**Create employee:**
```bash
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Priya",
    "lastName": "Mehta",
    "email": "priya.mehta@company.com",
    "department": "HR",
    "salary": 72000,
    "joiningDate": "2024-03-01"
  }'
```

**Filter by department:**
```bash
curl http://localhost:8080/api/v1/employees/department/Engineering
```

**Search by name:**
```bash
curl http://localhost:8080/api/v1/employees/search?name=rahul
```

**Salary range filter:**
```bash
curl "http://localhost:8080/api/v1/employees/salary-range?min=50000&max=90000"
```

---

## Key Features

**Layered Architecture** — strict separation across Controller → Service → Repository. Controller handles HTTP, Service owns business logic, Repository owns data access.

**DTO Pattern** — `EmployeeRequestDto` (with validation) and `EmployeeResponseDto` (with computed fields) keep the entity out of the API contract.

**Bean Validation** — `@NotBlank`, `@Email`, `@Positive`, `@NotNull` on the request DTO, enforced automatically via `@Valid`.

**Global Exception Handling** — `@RestControllerAdvice` catches `ResourceNotFoundException` (404), validation errors (400), duplicate email (409), and generic exceptions (500) — all returning structured JSON.

**Custom JPQL Queries** — `@Query` for salary range filtering and case-insensitive name search, alongside derived query methods like `findByDepartment`.

**Transaction Management** — `@Transactional` at class level with `readOnly = true` override on read methods for Hibernate performance optimization.

**SLF4J Logging** — parameterized log statements at key lifecycle points (create, update, delete).

---

## Environment & Security

- Real credentials live in `application-local.properties` (gitignored)
- `application.properties` is safe to commit — contains only non-sensitive config
- Production deployments use environment variables injected by the hosting platform
- TiDB Cloud connections use `sslMode=VERIFY_IDENTITY` for encrypted transport

### Gitignored files

```
application-local.properties
.env
target/
.idea/
*.iml
*.log
```

---

## Deployment

This app is deployed on **Railway** with **TiDB Cloud** as the database.

Live URL: `https://your-app.up.railway.app`

### Deploy your own

1. Push code to GitHub
2. Create a new project at [railway.app](https://railway.app)
3. Connect your GitHub repo
4. Set environment variables in Railway dashboard:

```
SPRING_DATASOURCE_URL      = jdbc:mysql://your-tidb-host:4000/employee_db?sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3
SPRING_DATASOURCE_USERNAME = your_username
SPRING_DATASOURCE_PASSWORD = your_password
SPRING_PROFILES_ACTIVE     = prod
```

5. Railway auto-deploys on every push to `main`.

---

## Author

**Kusum Pareek**
Java Backend Developer
[LinkedIn](https://www.linkedin.com/in/kusum-p-a54759191/) · [GitHub](https://github.com/KusumPareek99)