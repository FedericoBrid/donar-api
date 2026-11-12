# Donar+ API

RESTful backend for **Donar+**, a blood donation management system built with Spring Boot. The application provides authentication, role-based access control, blood center and blood request management, donation registration, and password recovery.

## Technologies

- **Java 21**
- **Spring Boot 4.1.1**
- **Spring Security**
- **Spring Data JPA / Hibernate**
- **MySQL 8.4**
- **JSON Web Tokens (JWT)**
- **Maven**
- **Spring Mail**
- **Brevo SMTP**
- **Swagger / OpenAPI**
- **Docker & Docker Compose**

## Features

- JWT-based authentication.
- Role-based authorization.
- User management.
- Blood center management.
- Blood request management.
- Blood donation registration and tracking.
- Donation statistics.
- Donation eligibility validation based on a recovery period.
- Password recovery through email.
- REST API documentation with Swagger/OpenAPI.
- Dockerized backend and MySQL database.

## Architecture

The application follows a layered architecture to separate responsibilities and facilitate maintenance and future scalability.

Main layers:

- **Controller:** handles HTTP requests and responses.
- **Service:** contains business logic and application rules.
- **Repository:** provides database access through Spring Data JPA.
- **Entity:** represents database tables and relationships.
- **DTO:** defines request and response data structures.
- **Security:** handles authentication, JWT processing, and authorization.
- **Exception:** centralizes application error handling.

The project is currently structured as a monolithic application, keeping its modules separated to facilitate potential future evolution into microservices.

## Authentication and Authorization

The API uses JWT (JSON Web Tokens) for stateless authentication.

### Authentication flow

1. The user submits their email and password.
2. The backend validates the credentials.
3. The application checks whether the user account is active.
4. A JWT is generated and returned after successful authentication.
5. The client sends the token in the `Authorization` header for protected endpoints.
6. Spring Security validates the token and establishes the authenticated user context.

Example:

```http
Authorization: Bearer <JWT_TOKEN>
```

### User roles

The application defines three roles:

| Role | Permissions |
|---|---|
| `ADMIN` | Full system access, including user and role management, blood center management, blood request management, and donation management. |
| `HEMOADMIN` | View users, manage blood centers and blood requests, view all donations, review donations associated with a blood request, and mark registered donations as completed. Can also register and cancel their own donations. |
| `USER` | Access donor functionality, view available blood requests, register donations, view their own donations, and cancel their own registered donations. |

### Donation permissions

Donation operations follow these authorization rules:

| Operation | ADMIN | HEMOADMIN | USER |
|---|:---:|:---:|:---:|
| View all donations | Yes | Yes | No |
| View donations by blood request | Yes | Yes | No |
| Register a donation | Yes | Yes | Yes |
| Cancel own registered donation | Yes | Yes | Yes |
| Complete a registered donation | Yes | Yes | No |
| View own donations | Yes | Yes | Yes |
| View donation statistics | Yes | Yes | Yes |

Donation registration and cancellation are available to authenticated users. A user can only cancel their own donation. Completing a donation is restricted to `ADMIN` and `HEMOADMIN`.

## Main Modules

### Users

Provides user registration, retrieval, and account status management.

Main responsibilities:

- Create users.
- Retrieve user information.
- Activate and deactivate accounts.
- Manage user roles according to authorization rules.
- Validate unique email addresses.

### Blood Centers

Manages blood center information, including:

- Name.
- Address.
- Phone number.
- Email.
- Public or private classification.
- Account status.

### Blood Requests

Manages blood donation requests associated with blood centers, blood types, and Rh factors.

Requests include:

- Blood type.
- Rh factor.
- Urgency level.
- Expiration date.
- Associated blood center.
- Request status.

Request statuses:

- `ACTIVE`
- `FULFILLED`
- `EXPIRED`
- `CANCELLED`

Urgency levels:

- `LOW`
- `MEDIUM`
- `HIGH`
- `CRITICAL`

### Donations

Handles the registration and tracking of donations associated with users and blood requests.

Donation statuses:

- `REGISTERED`
- `COMPLETED`
- `CANCELLED`

Business rules include:

- Donations can only be registered for active blood requests.
- A user can only have one registered donation at a time.
- Only registered donations can be cancelled.
- Users can only cancel their own donations.
- Only `ADMIN` and `HEMOADMIN` can mark donations as completed.
- After completing a donation, the user must wait for the configured recovery period before registering another donation.

The recovery period is configurable through the `DONATION_RECOVERY_DAYS` application property or its corresponding configuration value.

### Password Recovery

The application provides password recovery through email.

Endpoints:

- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`

Recovery flow:

1. The user requests a password reset.
2. The backend generates a cryptographically secure random token.
3. The original token is sent to the user's email address.
4. Only its SHA-256 hash is stored in the database.
5. The user opens the recovery link and enters a new password.
6. The backend validates the token and updates the password.
7. The token is marked as used.

Security considerations:

- Tokens are generated using `SecureRandom`.
- Tokens are stored as SHA-256 hashes.
- Tokens expire after **30 minutes**.
- Previous unused recovery requests are invalidated when a new request is created.
- Used tokens cannot be reused.
- The API does not disclose whether an email address is registered.

The recovery link is generated using the configured frontend URL:

```text
{FRONTEND_URL}/reset-password?token={token}
```

Email delivery is handled through Brevo SMTP using Spring Mail.

## API Documentation

Swagger UI is available after starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

## Requirements

For local development:

- Java 21 or later.
- Maven (or the included Maven Wrapper).
- MySQL 8.4 or compatible.
- Git.

For Docker execution:

- Docker.
- Docker Compose.

## Environment Variables

The application uses environment variables to manage configuration and sensitive information.

Create a `.env` file in the project root.

Example:

```env
# Database
DB_URL=jdbc:mysql://localhost:3306/donar-api
DB_USERNAME=root
DB_PASSWORD=

# JWT
JWT_SECRET=replace_with_a_secure_secret

# Email - Brevo SMTP
MAIL_HOST=smtp-relay.brevo.com
MAIL_PORT=587
MAIL_USERNAME=your_brevo_smtp_login
MAIL_PASSWORD=your_brevo_smtp_key
MAIL_FROM=your_verified_sender_email

# Frontend
FRONTEND_URL=http://localhost:5173
```

**Important:**

- Replace all example values with your own configuration.
- Never commit `.env` or expose credentials in the repository.
- Generate a secure JWT secret for each environment.
- Configure a verified sender address and valid SMTP credentials in Brevo.
- The empty database password is intended only for the local development setup. Use a strong password in production.
- `FRONTEND_URL` must point to the frontend application so password recovery emails contain the correct link.

The `.env.example` file contains the environment variable names and example values required to configure the application.

## Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/FedericoBrid/donar-api.git
cd donar-api
```

### 2. Configure environment variables

Create a `.env` file in the project root using `.env.example` as a reference.

Configure your local MySQL connection and the required JWT and email settings.

### 3. Create the database

Create a MySQL database named:

```sql
CREATE DATABASE `donar-api`;
```

Make sure the database credentials match your `.env` configuration.

### 4. Run the application

Using Maven Wrapper:

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

The backend will be available at:

```text
http://localhost:8080
```

## Running with Docker

The project includes a Dockerfile and Docker Compose configuration to run the backend and MySQL database together.

### 1. Configure environment variables

Create a `.env` file in the project root.

For the included local Docker configuration, the MySQL container allows an empty root password. This is intended exclusively for local development.

### 2. Build and start the containers

```bash
docker compose up --build -d
```

Docker Compose starts:

- **mysql:** MySQL 8.4 database.
- **backend:** Spring Boot API.

The backend waits for MySQL to become healthy before starting.

### 3. Check container status

```bash
docker compose ps
```

### 4. View backend logs

```bash
docker compose logs -f backend
```

### 5. Access the application

Backend:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

### 6. Stop the containers

```bash
docker compose down
```

This stops and removes the containers while preserving the database volume.

To remove the containers and the database volume:

```bash
docker compose down -v
```

**Warning:** The `-v` option deletes the persistent MySQL volume and its stored data.

## Database Persistence

Docker Compose uses a named volume:

```text
donar-mysql-data
```

This volume preserves database data when containers are stopped or recreated.

Removing containers with `docker compose down` does not delete the database volume.

## Testing

Run the automated tests using Maven Wrapper.

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux / macOS:**

```bash
./mvnw test
```

## Project Structure

The backend source code is organized by functional modules.

```text
src/
└── main/
    ├── java/
    │   └── com/donar/api/
    │       ├── auth/
    │       ├── bloodcenter/
    │       ├── bloodrequest/
    │       ├── common/
    │       ├── donation/
    │       ├── email/
    │       ├── passwordreset/
    │       ├── role/
    │       ├── security/
    │       └── user/
    │
    └── resources/
        └── application.properties

Dockerfile
.dockerignore
docker-compose.yml
.env.example
.gitignore
pom.xml
mvnw
mvnw.cmd
```

Each functional module contains its own entities, repositories, services, controllers, and DTOs where applicable.

## Frontend

The frontend application is developed separately using React and TypeScript.

Repository:

[Donar+ Frontend](https://github.com/FedericoBrid/donar-api-frontend)

Technologies:

- React 19.
- TypeScript.
- Vite.
- Material UI.
- React Router.

The frontend communicates with this backend through the REST API.

## Author

**Federico Brid**

- GitHub: [FedericoBrid](https://github.com/FedericoBrid)
- Backend: [donar-api](https://github.com/FedericoBrid/donar-api)
- Frontend: [donar-api-frontend](https://github.com/FedericoBrid/donar-api-frontend)
