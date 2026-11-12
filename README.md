# Donar+ API

REST API for **Donar+**, a blood donation management system designed to connect users, blood centers, and administrators to facilitate blood request management and donation registration.

This project is part of the migration of the original PHP application to a modern architecture based on **Java, Spring Boot, and React**.

## Technologies

- **Java 21** – Programming language.
- **Spring Boot 4** – Application framework.
- **Spring Web** – REST API development.
- **Spring Data JPA** – Data persistence and ORM.
- **Spring Security** – Authentication and authorization.
- **JWT** – Token-based authentication.
- **Hibernate** – ORM implementation.
- **MySQL 8.4** – Relational database.
- **Jakarta Validation** – Request validation.
- **Spring Mail** – Email integration.
- **Springdoc OpenAPI / Swagger** – API documentation.
- **Maven** – Dependency management and build automation.
- **Docker and Docker Compose** – Application containerization.

## Architecture

The backend follows a layered architecture to separate responsibilities and improve maintainability.

The main layers are:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic and application rules.
- **Repository:** Handles database access through Spring Data JPA.
- **Entity:** Represents database entities.
- **DTO:** Defines request and response data structures.
- **Security:** Handles authentication, JWT processing, and authorization.
- **Exception:** Centralizes application exception handling.

## Main Modules

- **Authentication:** Login, JWT generation, and authenticated user access.
- **Users:** User registration, information management, and account status.
- **Roles:** Role management and user-role assignments.
- **Blood Centers:** Blood center administration.
- **Blood Requests:** Blood request creation, management, and status control.
- **Donations:** Donation registration and status management.
- **Blood Types:** Blood type catalog.
- **Rh Factors:** Rh factor catalog.

## Authentication and Authorization

The API uses Spring Security and JWT for stateless authentication.

### Login

```http
POST /api/v1/auth/login
```

Users authenticate using their email and password.

When credentials are valid, the API returns user information and a JWT token.

The token must be included in subsequent protected requests:

```http
Authorization: Bearer <token>
```

### Roles

The system defines three roles:

- **ADMIN:** Full access to administrative operations, including user and role management.
- **HEMOADMIN:** Access to blood center operations and blood request management, without administrative user management permissions.
- **USER:** Basic application access, including blood request consultation and personal donation operations.

Authorization is enforced through Spring Security and method-level permissions.

## API Documentation

The API documentation is available through Swagger UI.

When the application is running:

http://localhost:8080/swagger-ui/index.html

OpenAPI specification:

http://localhost:8080/v3/api-docs

## Database

The application uses MySQL 8.4 as its relational database.

Spring Data JPA and Hibernate handle persistence and entity relationships.

The database schema is managed through Hibernate configuration.

The initial application data includes:

- Blood types.
- Rh factors.
- System roles.
- Initial administrator account.

## Environment Variables

The application uses environment variables to configure database connectivity, JWT authentication, email integration, frontend communication, and initial administrator creation.

Create a `.env` file in the backend root directory using `.env.example` as a reference.

Main configuration variables:

| Variable | Description |
|---|---|
| `DB_URL` | Database JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `MAIL_HOST` | SMTP server host |
| `MAIL_PORT` | SMTP server port |
| `MAIL_USERNAME` | SMTP username |
| `MAIL_PASSWORD` | SMTP password or API key |
| `MAIL_FROM` | Sender email address |
| `FRONTEND_URL` | Frontend application URL |
| `ADMIN_FIRST_NAME` | Initial administrator first name |
| `ADMIN_LAST_NAME` | Initial administrator last name |
| `ADMIN_BIRTH_DATE` | Initial administrator birth date |
| `ADMIN_EMAIL` | Initial administrator email |
| `ADMIN_PASSWORD` | Initial administrator password |

For the Docker environment, configure:

```env
FRONTEND_URL=http://localhost:3000
```

Do not commit the `.env` file or expose sensitive credentials.

## Running with Docker Compose

The project uses Docker Compose to run the complete application environment.

### Requirements

- Git.
- Docker Desktop.
- Docker Compose.

### Project structure

Both repositories must be cloned into the same parent directory because Docker Compose uses the frontend repository as a build context.

```text
donar-plus/
├── donar-api/
│   ├── docker-compose.yml
│   ├── Dockerfile
│   └── ...
└── donar-api-frontend/
    ├── Dockerfile
    └── ...
```

### 1. Clone the repositories

```bash
mkdir donar-plus
cd donar-plus

git clone https://github.com/FedericoBrid/donar-api.git
git clone https://github.com/FedericoBrid/donar-api-frontend.git
```

### 2. Configure environment variables

Navigate to the backend directory:

```bash
cd donar-api
```

Create the `.env` file using `.env.example` as a reference.

Set the required database, JWT, email, frontend, and administrator variables.

Make sure the frontend URL is:

```env
FRONTEND_URL=http://localhost:3000
```

### 3. Build and start the services

From the backend directory, run:

```bash
docker compose up --build -d
```

Docker Compose starts the following services:

| Service | Technology | Container | Port |
|---|---|---|---|
| MySQL | MySQL 8.4 | `donar-mysql` | 3306 |
| Backend | Spring Boot | `donar-api` | 8080 |
| Frontend | React + Nginx | `donar-frontend` | 3000 |

The backend waits for the MySQL health check before starting.

The frontend is built using the Dockerfile in the frontend repository and served through Nginx.

### 4. Access the application

Once all services are running:

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api/v1
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html

### 5. Check container status

```bash
docker compose ps
```

View service logs:

```bash
docker compose logs -f mysql
docker compose logs -f backend
docker compose logs -f frontend
```

### 6. Stop the services

```bash
docker compose down
```

This stops and removes the containers while preserving the MySQL data volume.

To also remove the database volume:

```bash
docker compose down -v
```

**Warning:** The `-v` option deletes the persistent database volume and all stored data.

## Running Locally Without Docker

The backend can also be executed independently for development.

### Requirements

- Java 21.
- Maven.
- MySQL 8.4.

### 1. Configure the database

Create a MySQL database and configure the required environment variables.

Make sure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` point to the local database.

### 2. Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

http://localhost:8080

### 3. Build the application

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

The executable JAR is generated in the `target/` directory.

## Project Repositories

Donar+ is divided into two independent projects:

| Project | Technology | Repository |
|---|---|---|
| Backend | Java and Spring Boot | [donar-api](https://github.com/FedericoBrid/donar-api) |
| Frontend | React, TypeScript and Vite | [donar-api-frontend](https://github.com/FedericoBrid/donar-api-frontend) |

The backend is responsible for business logic, data persistence, authentication, authorization, and REST API services.

The frontend provides the user interface and communicates with the backend through HTTP requests.

## Author

**Federico Brid**

- GitHub: [FedericoBrid](https://github.com/FedericoBrid)
- Backend: [donar-api](https://github.com/FedericoBrid/donar-api)
- Frontend: [donar-api-frontend](https://github.com/FedericoBrid/donar-api-frontend)
