# Donar+ API

API REST para la gestión de donaciones de sangre, usuarios, hemocentros, solicitudes de sangre y donaciones.

El proyecto forma parte de la reconstrucción de **Donar+**, originalmente desarrollado con PHP, utilizando una arquitectura moderna basada en **Spring Boot + Spring Security + JWT + MySQL**.

---

## 📋 Descripción

Donar+ permite gestionar el proceso de donación de sangre mediante una API REST segura.

La aplicación contempla diferentes perfiles de usuario y permite administrar:

* Usuarios
* Roles y permisos
* Hemocentros
* Solicitudes de sangre
* Donaciones
* Recuperación de contraseña
* Autenticación mediante JWT

El backend fue diseñado inicialmente como un **monolito modular**, manteniendo una separación clara entre responsabilidades y dejando abierta la posibilidad de evolucionar hacia una arquitectura distribuida en el futuro.

---

## 🚀 Tecnologías

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

---

## 🏗️ Arquitectura

El proyecto utiliza una arquitectura organizada por módulos de dominio.

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

Cada módulo mantiene separadas sus principales responsabilidades:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Las entidades representan el modelo persistente y los DTOs controlan los datos expuestos y recibidos por la API.

---

## 🔐 Seguridad

La API utiliza **Spring Security + JWT** para autenticar las solicitudes.

El flujo de autenticación es:

```text
POST /api/v1/auth/login
          │
          ▼
      AuthService
          │
          ▼
   Validación usuario
          │
          ▼
      JwtService
          │
          ▼
         JWT
          │
          ▼
   Cliente / Frontend
          │
          │ Authorization: Bearer JWT
          ▼
JwtAuthenticationFilter
          │
          ▼
CustomUserDetailsService
          │
          ▼
   SecurityContext
          │
          ▼
    @PreAuthorize
```

Las contraseñas se almacenan utilizando **BCrypt**.

Los roles son cargados desde la base de datos durante la autenticación de las solicitudes.

---

## 👥 Roles y permisos

La aplicación utiliza tres roles:

| Rol         | Descripción                                 |
| ----------- | ------------------------------------------- |
| `ADMIN`     | Administración completa del sistema         |
| `HEMOADMIN` | Administración de hemocentros y solicitudes |
| `USER`      | Funcionalidades destinadas al donante       |

### ADMIN

Puede:

* Gestionar usuarios
* Gestionar roles
* Gestionar hemocentros
* Gestionar solicitudes de sangre
* Gestionar donaciones
* Consultar información administrativa

### HEMOADMIN

Puede:

* Consultar usuarios
* Gestionar hemocentros
* Crear y modificar solicitudes de sangre
* Gestionar el estado de las donaciones

No posee las operaciones administrativas completas sobre usuarios.

### USER

Puede:

* Consultar su información
* Consultar solicitudes de sangre
* Registrarse para donar
* Cancelar una donación registrada
* Consultar su historial de donaciones

Los permisos se aplican mediante Spring Security y `@PreAuthorize`.

---

## 🩸 Funcionalidades

### Usuarios

* Registro
* Consulta
* Actualización
* Activación
* Desactivación
* Consulta del usuario autenticado

El email del usuario es único y no se modifica mediante la actualización normal del perfil.

---

### Roles

El sistema utiliza una relación muchos-a-muchos:

```text
User
  │
  │
  ▼
UserRole
  │
  ▼
Role
```

Esto permite que un usuario pueda tener múltiples roles activos.

---

### Hemocentros

Permite:

* Crear hemocentros
* Consultar hemocentros
* Actualizar información
* Activar
* Desactivar

Los hemocentros utilizan baja lógica mediante el campo `status`.

---

### Solicitudes de sangre

Permite:

* Crear solicitudes
* Consultar solicitudes
* Filtrar por estado
* Modificar solicitudes activas
* Marcar solicitudes como cumplidas
* Cancelar solicitudes

Estados disponibles:

```text
ACTIVE
FULFILLED
CANCELLED
EXPIRED
```

Flujo de estados:

```text
ACTIVE
 ├──→ FULFILLED
 ├──→ CANCELLED
 └──→ EXPIRED
```

---

### Donaciones

El sistema contempla el ciclo:

```text
REGISTERED
      │
      ├──→ CANCELLED
      │
      └──→ COMPLETED
```

Reglas principales:

* Un usuario no puede tener múltiples donaciones `REGISTERED` simultáneamente.
* Una donación registrada puede ser cancelada por el usuario.
* Una donación puede ser completada por `ADMIN` o `HEMOADMIN`.
* Luego de una donación completada existe un período de recuperación antes de volver a donar.
* Las donaciones no se eliminan físicamente.

El período de recuperación actualmente se configura mediante:

```properties
donation.recovery-days=60
```

---

### Recuperación de contraseña

La API permite:

```text
POST /api/v1/auth/forgot-password
POST /api/v1/auth/reset-password
```

Los tokens de recuperación:

* Son aleatorios.
* Tienen tiempo de expiración.
* Solo pueden utilizarse una vez.
* No revelan si un email existe al solicitar recuperación.

---

## 📚 Documentación de la API

La API utiliza **Swagger / OpenAPI**.

Con la aplicación ejecutándose:

```text
http://localhost:8080/swagger-ui/index.html
```

Es posible consultar y probar los endpoints disponibles.

Para endpoints protegidos se utiliza autenticación:

```text
Authorization: Bearer <JWT>
```

Swagger está configurado con un esquema `bearerAuth` para facilitar las pruebas.

---

## ⚙️ Configuración

La aplicación utiliza variables de entorno para la información sensible.

Ejemplo:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

Variables necesarias:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

El archivo `.env.example` contiene una referencia de las variables necesarias sin incluir información sensible.

---

## ▶️ Ejecución

### Requisitos

* Java
* Maven
* MySQL
* Base de datos configurada

### Clonar el proyecto

```bash
git clone <REPOSITORY_URL>
```

### Configurar variables de entorno

Configurar:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

### Ejecutar

Con Maven:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La API estará disponible en:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Pruebas

Los principales flujos funcionales fueron probados mediante Postman y Swagger.

Entre ellos:

* Login exitoso
* Login con credenciales inválidas
* Usuarios inactivos
* Registro de usuarios
* Asignación de roles
* Autenticación JWT
* Rutas protegidas
* Gestión de hemocentros
* Gestión de solicitudes
* Registro de donaciones
* Cancelación de donaciones
* Finalización de donaciones
* Restricción por período de recuperación
* Historial de donaciones
* Recuperación de contraseña
* Tokens de recuperación de un solo uso

---

## 📁 Estructura del proyecto

```text
src/
└── main/
    └── java/
        └── com/
            └── donar/
                └── api/
                    ├── auth/
                    ├── bloodcenter/
                    ├── bloodrequest/
                    ├── bloodtype/
                    ├── common/
                    ├── donation/
                    ├── passwordreset/
                    ├── rhfactor/
                    ├── role/
                    ├── security/
                    ├── user/
                    └── userrole/
```

---

## 🔮 Próximas mejoras

Algunas mejoras previstas para futuras versiones:

* Integración con frontend React
* Expiración automática de solicitudes
* Migraciones de base de datos con Flyway
* Configuración específica para producción
* Dockerización del backend
* Refresh tokens
* Mejoras de logging y observabilidad
* Despliegue de la aplicación

---

## 👨‍💻 Proyecto

**Donar+** es un proyecto de portfolio orientado al desarrollo Full Stack utilizando Java, Spring Boot y React.

Backend desarrollado con:

**Java + Spring Boot + Spring Security + JWT + MySQL**
