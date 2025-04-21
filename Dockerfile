FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .

# Resolve all dependencies to cache them in a separate Docker layer
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use OpenJDK JRE for runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Create a non-root user to run the application
RUN useradd -r -u 1001 -g root sfuser
USER sfuser

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]