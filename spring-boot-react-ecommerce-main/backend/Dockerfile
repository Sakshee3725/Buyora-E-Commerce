FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Expose port
EXPOSE 8082

# Run the application
CMD ["./mvnw", "spring-boot:run"]
