# =========================
# 🧱 BUILD STAGE
# =========================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Cache-friendly dependency layer
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests

# =========================
# 🚀 RUNTIME STAGE
# =========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Railway / Render / Docker
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]