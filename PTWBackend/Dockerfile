# Use the official Maven image as the base image for building the backend
FROM maven:3.8-openjdk-18-slim AS builder

# Set the working directory in the container
WORKDIR /src

# Copy the Maven project files to the container
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .

COPY src ./src
# Build the backend application
RUN mvn package -DskipTests


# Set the working directory in the container
WORKDIR /app

ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}
ENTRYPOINT java -jar PTWServer-0.0.1-SNAPSHOT.jar

