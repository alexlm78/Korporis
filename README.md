# Korporis

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.12.2-blue.svg)](https://quarkus.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Korporis is a modern employee and department management system built with Quarkus. It provides a RESTful API backend with a responsive Bootstrap-based frontend for managing organizational structure, employees, and departments.

## Features

- **Department Management**: Create, update, and delete departments with custom codes and descriptions
- **Employee Management**: Full CRUD operations for employee records with automatic employee code generation
- **RESTful API**: JSON-based REST endpoints for easy integration
- **Responsive UI**: Bootstrap-powered web interface for desktop and mobile devices
- **Database Flexibility**: Support for MySQL (primary) and SQLite
- **Active Record Pattern**: Simplified data access using Panache ORM
- **Bean Validation**: Built-in validation for data integrity

## Technology Stack

- **Framework**: Quarkus 3.12.2
- **Language**: Java 21
- **Build Tool**: Gradle
- **ORM**: Hibernate ORM with Panache
- **Database**: MySQL 8.x (SQLite also supported)
- **Frontend**: HTML5, Bootstrap 4.5.2, jQuery
- **Testing**: JUnit 5, REST Assured

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)** 21 or higher
- **Gradle** 7.x or higher (or use the included Gradle wrapper)
- **MySQL Server** 8.0 or higher
- **Git** (for cloning the repository)

## Installation

### 1. Clone the Repository

```sh
git clone https://github.com/alexlm78/Korporis.git
cd Korporis
```

### 2. Configure MySQL Database

Create the database and user for Korporis:

```sql
CREATE DATABASE korporis;
CREATE USER 'korporis'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON korporis.* TO 'korporis'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application Properties

Create or update `src/main/resources/application.properties`:

```properties
# Disable dev services
quarkus.devservices.enabled=false

# MySQL Datasource Configuration
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=korporis
quarkus.datasource.password=your_password
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/korporis
quarkus.datasource.jdbc.driver=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
quarkus.hibernate-orm.database.generation=update
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

#### Development Mode (with hot reload):

```sh
./gradlew quarkusDev
```

Or using Quarkus CLI (if installed):

```sh
quarkus dev
```

#### Production Mode:

```sh
java -jar build/quarkus-app/quarkus-run.jar
```

The application will be available at `http://localhost:8080`.

## API Endpoints

### Departments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/departamentos` | List all departments |
| POST | `/departamentos` | Create a new department |
| PUT | `/departamentos/{codigo}` | Update a department |
| DELETE | `/departamentos/{codigo}` | Delete a department |

### Employees

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/empleados` | List all employees |
| POST | `/empleados` | Create a new employee |
| PUT | `/empleados/{id}` | Update an employee |
| DELETE | `/empleados/{id}` | Delete an employee |

All endpoints accept and return JSON. Employee codes are automatically generated in the format `EMP-XXXX` upon creation.

## Project Structure

```
dev.kreaker.korporis/
├── model/              # JPA entities (Panache-based)
│   ├── Departamento.java
│   └── Empleado.java
└── resource/           # JAX-RS REST endpoints
    ├── DepartamentoResource.java
    └── EmpleadoResource.java
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

Build Docker image (JVM mode):

```sh
./gradlew build
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/korporis-jvm .
docker run -i --rm -p 8080:8080 quarkus/korporis-jvm
```

Build Docker image (native mode):

```sh
./gradlew build -Dquarkus.native.enabled=true
docker build -f src/main/docker/Dockerfile.native -t quarkus/korporis .
docker run -i --rm -p 8080:8080 quarkus/korporis
```

## Future Enhancements

Planned features include:
- **Attendance Tracking System**: Daily attendance management with work center assignments
- **Monthly Planning**: Employee scheduling across multiple work centers
- **Reporting**: Advanced analytics and attendance reports
- **Authentication & Authorization**: Role-based access control

See [ANALISIS.md](ANALISIS.md) for detailed technical specifications.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

**Alejandro Lopez**
- Email: [alejandro@kreaker.dev](mailto:alejandro@kreaker.dev)
- GitHub: [@alexlm78](https://github.com/alexlm78)

## Acknowledgments

- Built with [Quarkus](https://quarkus.io/) - Supersonic Subatomic Java
- UI powered by [Bootstrap](https://getbootstrap.com/)
- Database access via [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache)
