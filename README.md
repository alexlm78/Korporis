# Korporis - Employee & Department Management System

A RESTful API for managing employees and departments built with Spring Boot 4.0.2 and Java 25.

## Features

- **Employee Management**: Full CRUD operations for employees with hierarchical supervisor structure
- **Department Management**: Full CRUD operations for departments with manager assignment
- **Auto-generated Employee Codes**: Employees receive unique codes (EMP-XXXX format)
- **Validation**: Input validation using Jakarta Bean Validation
- **Error Handling**: Global exception handling with meaningful error responses
- **MySQL Database**: Production-ready MySQL 8.4 database with Docker Compose support

## Technology Stack

- **Java 25**
- **Spring Boot 4.0.2**
- **Spring Data JPA** with Hibernate ORM 7.2.1
- **MySQL 8.4** via Docker Compose
- **Lombok** for boilerplate reduction
- **Gradle** build system

## Getting Started

### Prerequisites

- Java 25 or higher
- Gradle 8.x (or use the included wrapper)
- Docker and Docker Compose (for MySQL database)

### Running with Docker Compose

1. Start the MySQL database:
```bash
docker compose up -d
```

2. Run the application:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080` connected to the MySQL database.

### Database Initialization

When starting the MySQL container for the first time, the database will be initialized with:
- Schema creation (`database/init/01-schema.sql`)
- Sample data (`database/init/02-data.sql`)

## API Endpoints

### Departments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/departments` | List all departments |
| GET | `/api/departments/{id}` | Get department by ID |
| GET | `/api/departments/code/{code}` | Get department by code |
| GET | `/api/departments/active` | List active departments |
| GET | `/api/departments/search?name={name}` | Search departments by name |
| POST | `/api/departments` | Create new department |
| PUT | `/api/departments/{id}` | Update department |
| PUT | `/api/departments/{id}/manager/{employeeId}` | Assign manager |
| DELETE | `/api/departments/{id}/manager` | Remove manager |
| PUT | `/api/departments/{id}/activate` | Activate department |
| PUT | `/api/departments/{id}/deactivate` | Deactivate department |
| DELETE | `/api/departments/{id}` | Delete department |

### Employees

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | List all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| GET | `/api/employees/code/{code}` | Get employee by code |
| GET | `/api/employees/dpi/{dpi}` | Get employee by DPI |
| GET | `/api/employees/email/{email}` | Get employee by email |
| GET | `/api/employees/department/{departmentId}` | List employees by department |
| GET | `/api/employees/status/{status}` | List employees by status |
| GET | `/api/employees/search?name={name}` | Search employees by name |
| GET | `/api/employees/{id}/subordinates` | Get employee's subordinates |
| POST | `/api/employees` | Create new employee |
| PUT | `/api/employees/{id}` | Update employee |
| PUT | `/api/employees/{id}/terminate` | Terminate employee |
| DELETE | `/api/employees/{id}` | Delete employee |

## Data Models

### Employee

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated ID |
| employeeCode | String | Auto-generated code (EMP-XXXX) |
| firstName | String | First name (required, max 50) |
| lastName | String | Last name (required, max 50) |
| dpi | String | DPI number (required, 13 digits, unique) |
| email | String | Email address (required, unique) |
| phone | String | Phone number (max 20) |
| address | String | Address (max 300) |
| birthDate | LocalDate | Date of birth |
| gender | Enum | MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY |
| hireDate | LocalDate | Hire date (required) |
| terminationDate | LocalDate | Termination date |
| position | String | Job position (required, max 100) |
| salary | BigDecimal | Salary (required, positive) |
| contractType | Enum | FULL_TIME, PART_TIME, CONTRACTOR, INTERN, TEMPORARY, FREELANCE |
| status | Enum | ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, SUSPENDED, RETIRED |
| departmentId | Long | Department reference |
| supervisorId | Long | Supervisor reference (hierarchical) |

### Department

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated ID |
| code | String | Department code (required, max 10, unique) |
| name | String | Department name (required, max 100) |
| description | String | Description (max 500) |
| location | String | Location (max 200) |
| active | Boolean | Active status |
| managerId | Long | Manager (employee) reference |

## Example Requests

### Create Employee

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Perez",
    "dpi": "1234567890123",
    "email": "juan.perez@company.com",
    "phone": "5555-1234",
    "address": "Zone 10, Guatemala City",
    "birthDate": "1990-05-15",
    "gender": "MALE",
    "hireDate": "2024-01-15",
    "position": "Software Developer",
    "salary": 10000.00,
    "contractType": "FULL_TIME",
    "departmentId": 1,
    "supervisorId": 2
  }'
```

### Create Department

```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "code": "MKT",
    "name": "Marketing",
    "description": "Marketing and advertising department",
    "location": "Building A, Floor 2"
  }'
```

### Update Employee

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "position": "Senior Software Developer",
    "salary": 15000.00
  }'
```

### Terminate Employee

```bash
curl -X PUT http://localhost:8080/api/employees/1/terminate
```

## Error Responses

The API returns structured error responses:

```json
{
  "timestamp": "2024-01-25T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999"
}
```

### Validation Errors

```json
{
  "timestamp": "2024-01-25T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/employees",
  "errors": {
    "firstName": "First name is required",
    "email": "Invalid email format"
  }
}
```

## Project Structure

```
src/main/java/dev/kreraker/korporis/
├── KorporisApplication.java          # Main application class
├── controller/
│   ├── DepartmentController.java     # Department REST endpoints
│   └── EmployeeController.java       # Employee REST endpoints
├── dto/
│   ├── CreateDepartmentRequest.java  # Department creation DTO
│   ├── CreateEmployeeRequest.java    # Employee creation DTO
│   ├── DepartmentDTO.java            # Department response DTO
│   ├── EmployeeDTO.java              # Employee response DTO
│   ├── UpdateDepartmentRequest.java  # Department update DTO
│   └── UpdateEmployeeRequest.java    # Employee update DTO
├── exception/
│   ├── BusinessException.java        # Business logic exception
│   ├── DuplicateResourceException.java # Duplicate resource exception
│   ├── ErrorResponse.java            # Error response DTO
│   ├── GlobalExceptionHandler.java   # Global exception handler
│   ├── ResourceNotFoundException.java # Resource not found exception
│   └── ValidationErrorResponse.java  # Validation error response DTO
├── model/
│   ├── ContractType.java             # Contract type enum
│   ├── Department.java               # Department entity
│   ├── Employee.java                 # Employee entity
│   ├── EmployeeStatus.java           # Employee status enum
│   └── Gender.java                   # Gender enum
├── repository/
│   ├── DepartmentRepository.java     # Department JPA repository
│   └── EmployeeRepository.java       # Employee JPA repository
└── service/
    ├── DepartmentService.java        # Department business logic
    └── EmployeeService.java          # Employee business logic

database/init/
├── 01-schema.sql                     # Database schema
└── 02-data.sql                       # Sample data
```

## Configuration Files

- `application.properties` - Main configuration with MySQL settings
- `compose.yaml` - Docker Compose configuration for MySQL

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
