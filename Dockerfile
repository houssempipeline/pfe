# Use a minimal and secure OpenJDK base image (Alpine-based)
FROM eclipse-temurin:17-jre-alpine

# Create app directory
WORKDIR /app

# Copy only the final JAR artifact
COPY target/DevSecOps-1.0.0-SNAPSHOT.jar app.jar

# Run the JAR using Java
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port your Spring Boot app uses
EXPOSE 7070

