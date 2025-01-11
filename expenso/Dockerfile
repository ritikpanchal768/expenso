# Use Maven for the build stage
FROM maven:3.8.4-openjdk AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml to the container's /app directory
COPY pom.xml .

# Download project dependencies
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Use OpenJDK for the runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/Expenso-0.0.1-SNAPSHOT.jar .

# Expose the application port
EXPOSE 9001

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/Expenso-0.0.1-SNAPSHOT.jar"]
