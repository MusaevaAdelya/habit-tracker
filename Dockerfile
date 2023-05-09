# Multi Stage build

# Base Image and name stage as "builder"
FROM maven:3.8.3-openjdk-17 AS builder

# Create App Directory inside our container
WORKDIR /app

# Copy files
COPY ./src ./src
COPY pom.xml .

RUN mvn -f /app/pom.xml clean package

#### 2nd Stage ####

FROM openjdk:17-jdk

WORKDIR /app

# Copy the Jar from the first Stage (builder) to the 2nd stage working directory
COPY --from=builder /app/target/habit-tracker.jar /app/

# Expose the port to the inner container communication network
EXPOSE 8080

# Run the Java Application
ENTRYPOINT [ "java","-jar","habit-tracker.jar"]