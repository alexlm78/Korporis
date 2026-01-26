# Korporis API Documentation

This document provides comprehensive documentation for all API endpoints available in the Korporis Employee & Department Management System.

**Base URL:** `http://localhost:8080`

**Content-Type:** `application/json`

---

## Table of Contents

1. [Department Endpoints](#department-endpoints)
   - [List All Departments](#list-all-departments)
   - [Get Department by ID](#get-department-by-id)
   - [Get Department by Code](#get-department-by-code)
   - [List Active Departments](#list-active-departments)
   - [Search Departments by Name](#search-departments-by-name)
   - [Create Department](#create-department)
   - [Update Department](#update-department)
   - [Assign Manager to Department](#assign-manager-to-department)
   - [Remove Manager from Department](#remove-manager-from-department)
   - [Activate Department](#activate-department)
   - [Deactivate Department](#deactivate-department)
   - [Delete Department](#delete-department)

2. [Employee Endpoints](#employee-endpoints)
   - [List All Employees](#list-all-employees)
   - [Get Employee by ID](#get-employee-by-id)
   - [Get Employee by Code](#get-employee-by-code)
   - [Get Employee by DPI](#get-employee-by-dpi)
   - [Get Employee by Email](#get-employee-by-email)
   - [List Employees by Department](#list-employees-by-department)
   - [List Employees by Status](#list-employees-by-status)
   - [Search Employees by Name](#search-employees-by-name)
   - [Get Employee Subordinates](#get-employee-subordinates)
   - [Create Employee](#create-employee)
   - [Update Employee](#update-employee)
   - [Terminate Employee](#terminate-employee)
   - [Delete Employee](#delete-employee)

3. [Data Models](#data-models)
   - [Department](#department-model)
   - [Employee](#employee-model)
   - [Enumerations](#enumerations)

4. [Error Responses](#error-responses)

---

## Department Endpoints

### List All Departments

Retrieves a list of all departments in the system.

**Endpoint:** `GET /api/departments`

**Response:** `200 OK`

```bash
curl -X GET http://localhost:8080/api/departments
```

**Response Body:**
```json
[
  {
    "id": 1,
    "code": "IT",
    "name": "Information Technology",
    "description": "Manages all IT infrastructure and software development",
    "location": "Building A, Floor 3",
    "active": true,
    "managerId": 1,
    "managerName": "Carlos Martinez",
    "employeeCount": 4,
    "createdAt": "2026-01-25T10:00:00",
    "updatedAt": "2026-01-25T10:00:00"
  }
]
```

---

### Get Department by ID

Retrieves a specific department by its unique identifier.

**Endpoint:** `GET /api/departments/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/departments/1
```

**Response Body:**
```json
{
  "id": 1,
  "code": "IT",
  "name": "Information Technology",
  "description": "Manages all IT infrastructure and software development",
  "location": "Building A, Floor 3",
  "active": true,
  "managerId": 1,
  "managerName": "Carlos Martinez",
  "employeeCount": 4,
  "createdAt": "2026-01-25T10:00:00",
  "updatedAt": "2026-01-25T10:00:00"
}
```

---

### Get Department by Code

Retrieves a specific department by its unique code.

**Endpoint:** `GET /api/departments/code/{code}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| code | String | The unique department code (e.g., "IT", "HR") |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/departments/code/IT
```

---

### List Active Departments

Retrieves all departments that are currently active.

**Endpoint:** `GET /api/departments/active`

**Response:** `200 OK`

```bash
curl -X GET http://localhost:8080/api/departments/active
```

---

### Search Departments by Name

Searches for departments whose name contains the specified search term (case-insensitive).

**Endpoint:** `GET /api/departments/search`

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| name | String | Search term to match against department names |

**Response:** `200 OK`

```bash
curl -X GET "http://localhost:8080/api/departments/search?name=tech"
```

---

### Create Department

Creates a new department in the system.

**Endpoint:** `POST /api/departments`

**Request Body:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| code | String | Yes | Unique department code (2-10 characters) |
| name | String | Yes | Department name (2-100 characters) |
| description | String | No | Department description (max 500 characters) |
| location | String | No | Physical location (max 200 characters) |

**Response:** `201 Created`

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

**Response Body:**
```json
{
  "id": 6,
  "code": "MKT",
  "name": "Marketing",
  "description": "Marketing and advertising department",
  "location": "Building A, Floor 2",
  "active": true,
  "managerId": null,
  "managerName": null,
  "employeeCount": 0,
  "createdAt": "2026-01-25T12:00:00",
  "updatedAt": "2026-01-25T12:00:00"
}
```

---

### Update Department

Updates an existing department's information.

**Endpoint:** `PUT /api/departments/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |

**Request Body:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | String | No | Department name (2-100 characters) |
| description | String | No | Department description (max 500 characters) |
| location | String | No | Physical location (max 200 characters) |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X PUT http://localhost:8080/api/departments/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Information Technology & Innovation",
    "description": "Updated description for IT department"
  }'
```

---

### Assign Manager to Department

Assigns an employee as the manager of a department.

**Endpoint:** `PUT /api/departments/{id}/manager/{employeeId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |
| employeeId | Long | The unique employee ID to assign as manager |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X PUT http://localhost:8080/api/departments/1/manager/1
```

---

### Remove Manager from Department

Removes the current manager from a department.

**Endpoint:** `DELETE /api/departments/{id}/manager`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X DELETE http://localhost:8080/api/departments/1/manager
```

---

### Activate Department

Activates a previously deactivated department.

**Endpoint:** `PUT /api/departments/{id}/activate`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X PUT http://localhost:8080/api/departments/1/activate
```

---

### Deactivate Department

Deactivates a department (soft delete).

**Endpoint:** `PUT /api/departments/{id}/deactivate`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X PUT http://localhost:8080/api/departments/1/deactivate
```

---

### Delete Department

Permanently deletes a department from the system.

**Endpoint:** `DELETE /api/departments/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique department ID |

**Response:** `204 No Content` or `404 Not Found`

```bash
curl -X DELETE http://localhost:8080/api/departments/1
```

---

## Employee Endpoints

### List All Employees

Retrieves a list of all employees in the system.

**Endpoint:** `GET /api/employees`

**Response:** `200 OK`

```bash
curl -X GET http://localhost:8080/api/employees
```

**Response Body:**
```json
[
  {
    "id": 1,
    "employeeCode": "EMP-0001",
    "firstName": "Carlos",
    "lastName": "Martinez",
    "fullName": "Carlos Martinez",
    "dpi": "1234567890123",
    "birthDate": "1980-05-15",
    "gender": "MALE",
    "email": "carlos.martinez@korporis.com",
    "phone": "5555-1234",
    "address": "Zone 10, Guatemala City",
    "hireDate": "2015-01-10",
    "terminationDate": null,
    "position": "Chief Technology Officer",
    "salary": 25000.00,
    "contractType": "FULL_TIME",
    "status": "ACTIVE",
    "departmentId": 1,
    "departmentName": "Information Technology",
    "departmentCode": "IT",
    "supervisorId": null,
    "supervisorName": null,
    "subordinateCount": 1,
    "createdAt": "2026-01-25T10:00:00",
    "updatedAt": "2026-01-25T10:00:00"
  }
]
```

---

### Get Employee by ID

Retrieves a specific employee by their unique identifier.

**Endpoint:** `GET /api/employees/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique employee ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/employees/1
```

---

### Get Employee by Code

Retrieves a specific employee by their unique employee code.

**Endpoint:** `GET /api/employees/code/{code}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| code | String | The unique employee code (e.g., "EMP-0001") |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/employees/code/EMP-0001
```

---

### Get Employee by DPI

Retrieves a specific employee by their DPI (national identification number).

**Endpoint:** `GET /api/employees/dpi/{dpi}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| dpi | String | The 13-digit DPI number |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/employees/dpi/1234567890123
```

---

### Get Employee by Email

Retrieves a specific employee by their email address.

**Endpoint:** `GET /api/employees/email/{email}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| email | String | The employee's email address |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/employees/email/carlos.martinez@korporis.com
```

---

### List Employees by Department

Retrieves all employees belonging to a specific department.

**Endpoint:** `GET /api/employees/department/{departmentId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| departmentId | Long | The unique department ID |

**Response:** `200 OK`

```bash
curl -X GET http://localhost:8080/api/employees/department/1
```

---

### List Employees by Status

Retrieves all employees with a specific employment status.

**Endpoint:** `GET /api/employees/status/{status}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| status | String | Employee status (ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, SUSPENDED, RETIRED) |

**Response:** `200 OK`

```bash
curl -X GET http://localhost:8080/api/employees/status/ACTIVE
```

---

### Search Employees by Name

Searches for employees whose first or last name contains the specified search term (case-insensitive).

**Endpoint:** `GET /api/employees/search`

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| name | String | Search term to match against employee names |

**Response:** `200 OK`

```bash
curl -X GET "http://localhost:8080/api/employees/search?name=carlos"
```

---

### Get Employee Subordinates

Retrieves all employees who report directly to a specific employee.

**Endpoint:** `GET /api/employees/{id}/subordinates`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique employee ID of the supervisor |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X GET http://localhost:8080/api/employees/1/subordinates
```

---

### Create Employee

Creates a new employee in the system. The employee code is auto-generated.

**Endpoint:** `POST /api/employees`

**Request Body:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| firstName | String | Yes | First name (2-50 characters) |
| lastName | String | Yes | Last name (2-50 characters) |
| dpi | String | Yes | DPI number (exactly 13 digits, unique) |
| email | String | Yes | Email address (unique, valid format) |
| phone | String | No | Phone number (max 20 characters) |
| address | String | No | Address (max 300 characters) |
| birthDate | String | No | Date of birth (YYYY-MM-DD format) |
| gender | String | No | Gender (MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY) |
| hireDate | String | Yes | Hire date (YYYY-MM-DD format, not in future) |
| position | String | Yes | Job position (2-100 characters) |
| salary | Number | Yes | Salary (positive number) |
| contractType | String | Yes | Contract type (see enumerations) |
| departmentId | Long | No | Department ID to assign |
| supervisorId | Long | No | Supervisor's employee ID |

**Response:** `201 Created`

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Perez",
    "dpi": "9876543210123",
    "email": "juan.perez@korporis.com",
    "phone": "5555-9876",
    "address": "Zone 14, Guatemala City",
    "birthDate": "1990-05-15",
    "gender": "MALE",
    "hireDate": "2026-01-15",
    "position": "Software Developer",
    "salary": 10000.00,
    "contractType": "FULL_TIME",
    "departmentId": 1,
    "supervisorId": 2
  }'
```

**Response Body:**
```json
{
  "id": 11,
  "employeeCode": "EMP-0011",
  "firstName": "Juan",
  "lastName": "Perez",
  "fullName": "Juan Perez",
  "dpi": "9876543210123",
  "birthDate": "1990-05-15",
  "gender": "MALE",
  "email": "juan.perez@korporis.com",
  "phone": "5555-9876",
  "address": "Zone 14, Guatemala City",
  "hireDate": "2026-01-15",
  "terminationDate": null,
  "position": "Software Developer",
  "salary": 10000.00,
  "contractType": "FULL_TIME",
  "status": "ACTIVE",
  "departmentId": 1,
  "departmentName": "Information Technology",
  "departmentCode": "IT",
  "supervisorId": 2,
  "supervisorName": "Maria Garcia",
  "subordinateCount": 0,
  "createdAt": "2026-01-25T12:00:00",
  "updatedAt": "2026-01-25T12:00:00"
}
```

---

### Update Employee

Updates an existing employee's information. Only provided fields will be updated.

**Endpoint:** `PUT /api/employees/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique employee ID |

**Request Body:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| firstName | String | No | First name (2-50 characters) |
| lastName | String | No | Last name (2-50 characters) |
| email | String | No | Email address (unique, valid format) |
| phone | String | No | Phone number (max 20 characters) |
| address | String | No | Address (max 300 characters) |
| birthDate | String | No | Date of birth (YYYY-MM-DD format) |
| gender | String | No | Gender (MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY) |
| position | String | No | Job position (2-100 characters) |
| salary | Number | No | Salary (positive number) |
| contractType | String | No | Contract type (see enumerations) |
| status | String | No | Employee status (see enumerations) |
| departmentId | Long | No | Department ID to assign |
| supervisorId | Long | No | Supervisor's employee ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "position": "Senior Software Developer",
    "salary": 15000.00
  }'
```

---

### Terminate Employee

Terminates an employee's employment. Sets status to TERMINATED and records the termination date.

**Endpoint:** `PUT /api/employees/{id}/terminate`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique employee ID |

**Response:** `200 OK` or `404 Not Found`

```bash
curl -X PUT http://localhost:8080/api/employees/1/terminate
```

---

### Delete Employee

Permanently deletes an employee from the system.

**Endpoint:** `DELETE /api/employees/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | The unique employee ID |

**Response:** `204 No Content` or `404 Not Found`

```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

---

## Data Models

### Department Model

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated unique identifier |
| code | String | Unique department code (2-10 characters) |
| name | String | Department name (2-100 characters) |
| description | String | Optional description (max 500 characters) |
| location | String | Physical location (max 200 characters) |
| active | Boolean | Whether the department is active |
| managerId | Long | ID of the employee who manages this department |
| managerName | String | Full name of the manager |
| employeeCount | Integer | Number of employees in the department |
| createdAt | DateTime | Timestamp when the department was created |
| updatedAt | DateTime | Timestamp when the department was last updated |

### Employee Model

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated unique identifier |
| employeeCode | String | Auto-generated employee code (EMP-XXXX format) |
| firstName | String | First name (2-50 characters) |
| lastName | String | Last name (2-50 characters) |
| fullName | String | Computed full name (firstName + lastName) |
| dpi | String | National ID number (exactly 13 digits, unique) |
| birthDate | Date | Date of birth |
| gender | String | Gender enumeration value |
| email | String | Email address (unique) |
| phone | String | Phone number (max 20 characters) |
| address | String | Physical address (max 300 characters) |
| hireDate | Date | Employment start date |
| terminationDate | Date | Employment end date (if terminated) |
| position | String | Job title/position (2-100 characters) |
| salary | Decimal | Monthly salary (positive number) |
| contractType | String | Type of employment contract |
| status | String | Current employment status |
| departmentId | Long | ID of the assigned department |
| departmentName | String | Name of the assigned department |
| departmentCode | String | Code of the assigned department |
| supervisorId | Long | ID of the direct supervisor |
| supervisorName | String | Full name of the supervisor |
| subordinateCount | Integer | Number of direct reports |
| createdAt | DateTime | Timestamp when the employee was created |
| updatedAt | DateTime | Timestamp when the employee was last updated |

### Enumerations

#### Gender
| Value | Description |
|-------|-------------|
| MALE | Male |
| FEMALE | Female |
| OTHER | Other |
| PREFER_NOT_TO_SAY | Prefer not to say |

#### Contract Type
| Value | Description |
|-------|-------------|
| FULL_TIME | Full-time employment |
| PART_TIME | Part-time employment |
| CONTRACTOR | Independent contractor |
| INTERN | Internship |
| TEMPORARY | Temporary employment |
| FREELANCE | Freelance work |

#### Employee Status
| Value | Description |
|-------|-------------|
| ACTIVE | Currently employed and active |
| INACTIVE | Temporarily inactive |
| ON_LEAVE | On leave (vacation, medical, etc.) |
| TERMINATED | Employment terminated |
| SUSPENDED | Temporarily suspended |
| RETIRED | Retired from employment |

---

## Error Responses

### Standard Error Response

All error responses follow this structure:

```json
{
  "timestamp": "2026-01-25T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999"
}
```

### Validation Error Response

When request validation fails:

```json
{
  "timestamp": "2026-01-25T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/employees",
  "errors": {
    "firstName": "First name is required",
    "email": "Invalid email format",
    "dpi": "DPI must contain only 13 digits"
  }
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Resource deleted successfully |
| 400 | Bad Request - Invalid request data or validation error |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Duplicate resource (e.g., duplicate email or DPI) |
| 500 | Internal Server Error - Unexpected server error |

---

## Quick Reference

### Department Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/departments` | List all departments |
| GET | `/api/departments/{id}` | Get department by ID |
| GET | `/api/departments/code/{code}` | Get department by code |
| GET | `/api/departments/active` | List active departments |
| GET | `/api/departments/search?name={name}` | Search by name |
| POST | `/api/departments` | Create department |
| PUT | `/api/departments/{id}` | Update department |
| PUT | `/api/departments/{id}/manager/{employeeId}` | Assign manager |
| DELETE | `/api/departments/{id}/manager` | Remove manager |
| PUT | `/api/departments/{id}/activate` | Activate department |
| PUT | `/api/departments/{id}/deactivate` | Deactivate department |
| DELETE | `/api/departments/{id}` | Delete department |

### Employee Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | List all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| GET | `/api/employees/code/{code}` | Get employee by code |
| GET | `/api/employees/dpi/{dpi}` | Get employee by DPI |
| GET | `/api/employees/email/{email}` | Get employee by email |
| GET | `/api/employees/department/{departmentId}` | List by department |
| GET | `/api/employees/status/{status}` | List by status |
| GET | `/api/employees/search?name={name}` | Search by name |
| GET | `/api/employees/{id}/subordinates` | Get subordinates |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| PUT | `/api/employees/{id}/terminate` | Terminate employee |
| DELETE | `/api/employees/{id}` | Delete employee |
