FROM eclipse-temurin:21-jdk

WORKDIR /app

# Gradle wrapper and project configuration
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Application source
COPY src ./src

# Build Spring Boot application
RUN ./gradlew bootJar --no-daemon

# Render provides PORT environment variable
EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar build/libs/portfolio-java-spring-1.0.1.jar"]