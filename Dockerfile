# Use a fixed version, not "latest"
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy application JAR
COPY target/cyber-security-pipeline-example-project-0.0.1-SNAPSHOT.jar app.jar

# Add a non-root user and group
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

# Switch to non-root user
USER appuser

# Add healthcheck to ensure the app is running
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
