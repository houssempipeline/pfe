# Use an official OpenJDK base image
FROM openjdk:17
# Set the working directory in the container
WORKDIR /app

# Copy the jar file from your target directory to the container
COPY target/DevSecOps-1.0.0-SNAPSHOT.jar app.jar

# Command to run your app
ENTRYPOINT ["java", "-jar", "app.jar"]


#Expose port 7070
EXPOSE 7070
