# Use a base image with Maven for building
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Use a smaller base image for the runtime environment
FROM openjdk:17
WORKDIR /app

# Create a non-root user and group with a specific UID and GID
RUN groupadd -g 1000 appgroup && \
    useradd -u 1000 appuser -g appgroup && \
    chown appuser:appgroup /app

# Switch to the non-root user
USER appuser

EXPOSE 8081

# Copy the JAR file from the build stage
COPY --from=builder /app/target/claim-integration-service-1.0.jar /app/claim-integration-service.jar

# Specify the default command to run on container start
CMD ["java", "-jar", "/app/claim-integration-service.jar"]
