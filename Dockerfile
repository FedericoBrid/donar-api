
# =========================
# Etapa 1: Build
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar Maven Wrapper y configuración
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación
RUN ./mvnw clean package -DskipTests

# =========================
# Etapa 2: Runtime
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Crear usuario sin privilegios
RUN groupadd -r spring && useradd -r -g spring spring

# Copiar JAR generado
COPY --from=build /app/target/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
