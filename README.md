# Korporis - Employee & Department Management System

A RESTful API for managing employees and departments built with Quarkus 3.32.3 and Java 25.

## Features

- Employee Management: Full CRUD with hierarchical supervisor structure
- Department Management: Full CRUD with manager assignment and activation/deactivation
- Auto-generated Employee Codes (EMP-XXXX format)
- Input validation with Jakarta Bean Validation
- Global exception handling with structured error responses
- MySQL 8.4 database with Docker Compose support
- Database schema auto-generated via Hibernate ORM

## Technology Stack

| Component        | Technology                 |
|------------------|----------------------------|
| Language         | Java 25                    |
| Framework        | Quarkus 3.32.3             |
| Build Tool       | Gradle 9.3.1               |
| ORM              | Hibernate ORM with Panache |
| Validation       | Jakarta Bean Validation    |
| REST             | RESTEasy with JSON-B       |
| Database         | MySQL 8.4                  |
| Containerization | Docker Compose             |

## Prerequisites

- Java 25 or higher
- Gradle 9.x (or use the included wrapper)
- Docker and Docker Compose (for MySQL)

## Getting Started

### 1. Start the database

```bash
docker compose up -d
```

### 2. Run the application

```bash
./gradlew quarkusDev
```

The application starts on `http://localhost:9087`.

### Database Initialization

On first start, the MySQL container runs the init scripts in order:

- `database/init/00-init.sql`
- `database/init/01-schema.sql`
- `database/init/02-data.sql`

### Environment Variables

| Variable         | Default                                | Description          |
|------------------|----------------------------------------|----------------------|
| `MYSQL_URL`      | `jdbc:mysql://localhost:3306/korporis` | JDBC connection URL  |
| `MYSQL_USERNAME` | `korporis`                             | Database username    |
| `MYSQL_PASSWORD` | `k0rp0r15`                             | Database password    |

## API Endpoints

### Departments (`/api/departments`)

| Method | Endpoint                               | Description                            |
|--------|----------------------------------------|----------------------------------------|
| GET    | `/api/departments`                     | List all (optional `?activeOnly=true`) |
| GET    | `/api/departments/{id}`                | Get by ID                              |
| GET    | `/api/departments/code/{code}`         | Get by code                            |
| GET    | `/api/departments/search?name=`        | Search by name                         |
| GET    | `/api/departments/{id}/employee-count` | Get employee count                     |
| POST   | `/api/departments`                     | Create department                      |
| PUT    | `/api/departments/{id}`                | Update department                      |
| PATCH  | `/api/departments/{id}/activate`       | Activate department                    |
| PATCH  | `/api/departments/{id}/deactivate`     | Deactivate department                  |
| DELETE | `/api/departments/{id}`                | Delete department                      |

### Employees (`/api/employees`)

| Method | Endpoint                                   | Description                            |
|--------|--------------------------------------------|----------------------------------------|
| GET    | `/api/employees`                           | List all (optional `?activeOnly=true`) |
| GET    | `/api/employees/{id}`                      | Get by ID                              |
| GET    | `/api/employees/code/{code}`               | Get by employee code                   |
| GET    | `/api/employees/department/{departmentId}` | List by department                     |
| GET    | `/api/employees/{id}/subordinates`         | Get subordinates                       |
| GET    | `/api/employees/search?name=`              | Search by name                         |
| POST   | `/api/employees`                           | Create employee                        |
| PUT    | `/api/employees/{id}`                      | Update employee                        |
| PATCH  | `/api/employees/{id}/terminate`            | Terminate employee                     |
| DELETE | `/api/employees/{id}`                      | Delete employee                        |

## Data Models

### Department

| Field       | Type          | Description                        |
|-------------|---------------|------------------------------------|
| id          | Long          | Auto-generated ID                  |
| code        | String        | Unique code (2-10 chars)           |
| name        | String        | Name (2-100 chars)                 |
| description | String        | Description (max 500)              |
| location    | String        | Location (max 200)                 |
| active      | Boolean       | Active status (default: true)      |
| manager     | Employee      | Manager reference                  |
| createdAt   | LocalDateTime | Creation timestamp                 |
| updatedAt   | LocalDateTime | Last update timestamp              |

### Employee

| Field           | Type           | Description                                                    |
|-----------------|----------------|----------------------------------------------------------------|
| id              | Long           | Auto-generated ID                                              |
| employeeCode    | String         | Unique code (EMP-XXXX)                                         |
| firstName       | String         | First name (2-50 chars, required)                              |
| lastName        | String         | Last name (2-50 chars, required)                               |
| dpi             | String         | DPI number (13 digits, unique, required)                       |
| email           | String         | Email (unique, required)                                       |
| phone           | String         | Phone (max 20)                                                 |
| address         | String         | Address (max 300)                                              |
| birthDate       | LocalDate      | Date of birth                                                  |
| gender          | Gender         | MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY                         |
| hireDate        | LocalDate      | Hire date (required)                                           |
| terminationDate | LocalDate      | Termination date                                               |
| position        | String         | Job position (2-100 chars, required)                           |
| salary          | BigDecimal     | Salary (positive, required)                                    |
| contractType    | ContractType   | FULL_TIME, PART_TIME, CONTRACTOR, INTERN, TEMPORARY, FREELANCE |
| status          | EmployeeStatus | ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, SUSPENDED, RETIRED     |
| department      | Department     | Department reference                                           |
| supervisor      | Employee       | Supervisor reference (hierarchical)                            |

## Project Structure

```
src/main/java/dev/kreraker/korporis/
├── KorporisApplication.java
├── controller/
│   ├── DepartmentController.java
│   └── EmployeeController.java
├── dto/
│   ├── CreateDepartmentRequest.java
│   ├── CreateEmployeeRequest.java
│   ├── DepartmentDTO.java
│   ├── EmployeeDTO.java
│   ├── UpdateDepartmentRequest.java
│   └── UpdateEmployeeRequest.java
├── exception/
│   ├── BusinessException.java
│   ├── DuplicateResourceException.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── ValidationErrorResponse.java
├── model/
│   ├── ContractType.java
│   ├── Department.java
│   ├── Employee.java
│   ├── EmployeeStatus.java
│   └── Gender.java
├── repository/
│   ├── DepartmentRepository.java
│   └── EmployeeRepository.java
└── service/
    ├── DepartmentService.java
    └── EmployeeService.java

database/init/
├── 00-init.sql
├── 01-schema.sql
└── 02-data.sql
```

## Configuration

Application configuration is in `src/main/resources/application.yaml` (YAML format).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
