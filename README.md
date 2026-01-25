# Korporis

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-green.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Korporis is a modern employee and department management system built with Spring Boot. It provides a RESTful API backend with a responsive Bootstrap-based frontend for managing organizational structure, employees, and departments.

## Features

- **Department Management**: Create, update, and delete departments with custom codes and descriptions
- **Employee Management**: Full CRUD operations for employee records with automatic employee code generation
- **RESTful API**: JSON-based REST endpoints for easy integration
- **Responsive UI**: Bootstrap-powered web interface for desktop and mobile devices
- **Database Flexibility**: Support for MySQL (primary) and H2 for development
- **Spring Data JPA**: Simplified data access using Spring Data repositories
- **Bean Validation**: Built-in validation for data integrity

## Technology Stack

- **Framework**: Spring Boot 4.0.2
- **Language**: Java 25
- **Build Tool**: Gradle 8.x
- **ORM**: Hibernate ORM with Spring Data JPA
- **Database**: MySQL 8.x (H2 also supported for development)
- **Frontend**: HTML5, Bootstrap 4.5.2, jQuery
- **Testing**: JUnit 5, Spring Boot Test

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)** 25 or higher
- **Gradle** 8.x or higher (or use the included Gradle wrapper)
- **MySQL Server** 8.0 or higher (optional, H2 can be used for development)
- **Git** (for cloning the repository)

## Installation

### 1. Clone the Repository

```sh
git clone https://github.com/alexlm78/Korporis.git
cd Korporis
```

### 2. Configure MySQL Database (Optional)

Create the database and user for Korporis:

```sql
CREATE DATABASE korporis;
CREATE USER 'korporis'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON korporis.* TO 'korporis'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application Properties

Create or update `src/main/resources/application.properties`:

#### For MySQL:

```properties
spring.application.name=korporis

# MySQL Datasource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/korporis
spring.datasource.username=korporis
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

#### For H2 (Development):

```properties
spring.application.name=korporis

# H2 Datasource Configuration
spring.datasource.url=jdbc:h2:mem:korporis
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

**Note**: Replace `your_password` with the password you set during MySQL user creation.

### 4. Build the Application

Using Gradle wrapper (recommended):

```sh
./gradlew build
```

Or if you have Gradle installed globally:

```sh
gradle build
```

### 5. Run the Application

#### Development Mode:

```sh
./gradlew bootRun
```

#### Production Mode:

```sh
java -jar build/libs/korporis-0.0.1-SNAPSHOT.jar
```

The application will be available at `http://localhost:8080`.

## API Endpoints

### Departments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/departments` | List all departments |
| POST | `/departments` | Create a new department |
| PUT | `/departments/{code}` | Update a department |
| DELETE | `/departments/{code}` | Delete a department |

### Employees

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/employees` | List all employees |
| POST | `/employees` | Create a new employee |
| PUT | `/employees/{id}` | Update an employee |
| DELETE | `/employees/{id}` | Delete an employee |

All endpoints accept and return JSON. Employee codes are automatically generated in the format `EMP-XXXX` upon creation.

## Project Structure

```
dev.kreaker.korporis/
├── model/              # JPA entities
│   ├── Department.java
│   └── Employee.java
├── repository/         # Spring Data JPA repositories
│   ├── DepartmentRepository.java
│   └── EmployeeRepository.java
└── resource/           # REST controllers (to be migrated to controller/)
    ├── DepartmentResource.java
    └── EmployeeResource.java
```

## Testing

Run all tests:

```sh
./gradlew test
```

Run a specific test:

```sh
./gradlew test --tests dev.kreaker.GreetingResourceTest
```

## Docker Support

Build Docker image:

```sh
./gradlew bootBuildImage
```

Run the Docker image:

```sh
docker run -p 8080:8080 korporis:0.0.1-SNAPSHOT
```

## Future Enhancements

Planned features include:
- **Attendance Tracking System**: Daily attendance management with work center assignments
- **Monthly Planning**: Employee scheduling across multiple work centers
- **Reporting**: Advanced analytics and attendance reports
- **Authentication & Authorization**: Role-based access control with Spring Security

See [ANALISIS.md](doxs/ANALISIS.md) for detailed technical specifications.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

**Alejandro Lopez**
- Email: [alejandro@kreaker.dev](mailto:alejandro@kreaker.dev)
- GitHub: [@alexlm78](https://github.com/alexlm78)

## Acknowledgments

- Built with [Spring Boot](https://spring.io/projects/spring-boot) - The Modern Java Framework
- UI powered by [Bootstrap](https://getbootstrap.com/)
- Database access via [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

**Alejandro Lopez**
- Email: [alejandro@kreaker.dev](mailto:alejandro@kreaker.dev)
- GitHub: [@alexlm78](https://github.com/alexlm78)

## Acknowledgments

- Built with [Spring Boot](https://spring.io/projects/spring-boot) - The Modern Java Framework
- UI powered by [Bootstrap](https://getbootstrap.com/)
- Database access via [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

