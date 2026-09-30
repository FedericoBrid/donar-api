# Donar+ API

REST API for blood donation management built with Java, Spring Boot, Spring Security and MySQL.

This project is the backend of **Donar+**, a blood donation management platform originally developed with PHP/MVC and later rebuilt using a modern REST architecture.

The application manages users, roles, blood centers, blood requests and donations, with authentication and authorization based on JWT.

---

## 🚀 Technologies

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* BCrypt
* Bean Validation
* MySQL
* Maven
* Swagger / OpenAPI
* Lombok

---

## 📋 Features

### Authentication & Security

* User login
* JWT authentication
* Protected endpoints
* Role-based authorization
* BCrypt password encryption
* User activation and deactivation
* Password recovery with expiring tokens

### Users

* User registration
* Retrieve users
* Retrieve authenticated user
* Update user information
* Activate users
* Deactivate users
* Unique email validation

### Roles

* `ADMIN`
* `HEMOADMIN`
* `USER`

Users and roles are managed through a many-to-many relationship, allowing a user to have multiple active roles.

### Blood Centers

* Create blood centers
* Retrieve blood centers
* Update blood centers
* Activate blood centers
* Deactivate blood centers
* Logical deletion through status

### Blood Requests

* Create blood requests
* Retrieve blood requests
* Filter requests by status
* Update active requests
* Complete requests
* Cancel requests
* Request expiration support

### Donations

* Register a donation
* Cancel a registered donation
* Complete a donation
* View personal donation history
* View donors associated with a blood request
* Prevent multiple simultaneous registered donations
* Recovery period between completed donations

---

## 🏗️ Architecture

The backend follows a modular architecture organized by domain:

```text
┌─────────────────────────┐
│       Controller        │
│        REST API         │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│        Service          │
│    Business Logic       │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Repository        │
│     Spring Data JPA     │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│         MySQL           │
│       Database          │
└─────────────────────────┘
```

The project is organized by domain:

```text
com.donar.api
│
├── auth
├── user
├── userrole
├── role
├── bloodcenter
├── bloodrequest
├── donation
├── passwordreset
├── bloodtype
├── rhfactor
├── security
└── common
```

Each module separates its main responsibilities between controllers, services, repositories, entities and DTOs.

---

## 🔐 Authentication & Authorization

The API uses **Spring Security and JWT**.

The authentication flow is:

```text
┌───────────────┐
│    Client     │
└───────┬───────┘
        │
        │ POST /api/v1/auth/login
        ▼
┌────────────────────┐
│    AuthService     │
└─────────┬──────────┘
          │
          │ Validate credentials
          ▼
┌────────────────────┐
│     JwtService     │
└─────────┬──────────┘
          │
          │ JWT
          ▼
┌────────────────────┐
│      Client        │
└─────────┬──────────┘
          │
          │ Authorization: Bearer JWT
          ▼
┌────────────────────────────┐
│ JwtAuthenticationFilter    │
└─────────────┬──────────────┘
              ▼
┌────────────────────────────┐
│ CustomUserDetailsService   │
└─────────────┬──────────────┘
              ▼
┌────────────────────────────┐
│      SecurityContext       │
└─────────────┬──────────────┘
              ▼
┌────────────────────────────┐
│       @PreAuthorize        │
└────────────────────────────┘
```

Passwords are encrypted using BCrypt.

Roles are loaded from the database when authenticating requests, allowing role changes to take effect without storing authorization data permanently inside the JWT.

---

## 👥 Roles & Permissions

| Role        | Description                                   |
| ----------- | --------------------------------------------- |
| `ADMIN`     | Full system administration                    |
| `HEMOADMIN` | Blood center and blood request administration |
| `USER`      | Donor functionality                           |

### ADMIN

Can manage:

* Users
* Roles
* Blood centers
* Blood requests
* Donations
* Administrative information

### HEMOADMIN

Can:

* View users
* Manage blood centers
* Create and update blood requests
* Manage donation status

### USER

Can:

* View their own information
* View blood requests
* Register to donate
* Cancel a registered donation
* View their donation history

Authorization is implemented using Spring Security and `@PreAuthorize`.

---

## 🩸 Donation Flow

Donations follow a controlled state flow:

```text
REGISTERED
     │
     ├──────────────► CANCELLED
     │
     └──────────────► COMPLETED
```

Main business rules:

* A user cannot have multiple `REGISTERED` donations simultaneously.
* A registered donation can be cancelled by the user.
* A donation can be completed by `ADMIN` or `HEMOADMIN`.
* Completed donations record the date of donation.
* A recovery period is required before donating again.
* Donations are not physically deleted.

The recovery period is configurable:

```properties
donation.recovery-days=60
```

---

## 🔑 Password Recovery

The API provides password recovery through:

```text
POST /api/v1/auth/forgot-password
POST /api/v1/auth/reset-password
```

Recovery tokens:

* Are randomly generated.
* Have an expiration time.
* Can only be used once.
* Do not reveal whether an email exists in the system.

---

## 📚 API Documentation

The project uses **Swagger / OpenAPI** for API documentation and testing.

After starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Protected endpoints require:

```text
Authorization: Bearer <JWT>
```

Swagger includes a `bearerAuth` security scheme to simplify authenticated API testing.

---

## 📦 Requirements

Before starting the application, install:

* Java
* Maven
* MySQL
* Git

Postman is optional and can be used to test the REST API.

---

## ⚙️ Environment Configuration

Sensitive configuration is managed through environment variables.

Example:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

Required environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

The repository includes an `.env.example` file as a reference.

**Do not commit real credentials or JWT secrets to the repository.**

---

## ▶️ Run the Application

### 1. Clone the repository

```bash
git clone https://github.com/FedericoBrid/donar-api.git
```

### 2. Move into the project directory

```bash
cd donar-api
```

### 3. Configure environment variables

Configure:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

### 4. Run with Maven

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Testing

The main application flows have been tested using **Postman and Swagger**.

Tested functionality includes:

* User registration
* User login
* Invalid credentials
* Inactive users
* JWT authentication
* Protected endpoints
* Role-based authorization
* User management
* Role assignment
* Blood center management
* Blood request management
* Donation registration
* Donation cancellation
* Donation completion
* Donation recovery period
* Donation history
* Password recovery
* Expiring password reset tokens
* Single-use password reset tokens

---

## 📁 Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── donar/
    │           └── api/
    │               ├── auth/
    │               ├── bloodcenter/
    │               ├── bloodrequest/
    │               ├── bloodtype/
    │               ├── common/
    │               ├── donation/
    │               ├── passwordreset/
    │               ├── rhfactor/
    │               ├── role/
    │               ├── security/
    │               ├── user/
    │               └── userrole/
    │
    └── resources/
        └── application.properties
```

---

## 🌐 Frontend

The React frontend will be maintained in a **separate repository**, following the same approach used in the Employee Management project.

The complete Donar+ architecture will be:

```text
┌───────────────────────────────┐
│            React              │
│           Frontend            │
└──────────────┬────────────────┘
               │
               │ HTTP / REST
               ▼
┌───────────────────────────────┐
│         Spring Boot           │
│           Backend             │
│            :8080              │
└──────────────┬────────────────┘
               │
               │ JPA / JDBC
               ▼
┌───────────────────────────────┐
│             MySQL             │
│             :3306             │
└───────────────────────────────┘
```

Frontend repository:

> Will be added when the React application is completed.

---

## 🔮 Future Improvements

Planned improvements include:

* React frontend
* Automatic blood request expiration
* Flyway database migrations
* Dockerization
* Refresh tokens
* Production configuration
* Improved logging and observability
* Application deployment
* Automated testing

---

## 👨‍💻 Project

Donar+ is a portfolio project focused on **Full Stack development with Java, Spring Boot and React**.

The backend represents a modernization of an earlier PHP/MVC version of the application, introducing a REST API architecture, JWT authentication, role-based authorization and improved separation of responsibilities.

### Backend

**Java · Spring Boot · Spring Security · JWT · MySQL · Swagger/OpenAPI**

### Frontend

**React · Vite**

Frontend and backend are maintained as separate repositories.
