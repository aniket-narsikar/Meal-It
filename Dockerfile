# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy Maven POM and download dependencies for caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the production JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Production Runtime Image
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user for security best practices
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built JAR artifact from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose default port (Railway automatically maps $PORT at runtime)
EXPOSE 8081

# JVM flags optimized for container environments
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
