# Stage 1: Build the Application
FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Package the application (e.g., as a JAR file)
RUN ./mvnw package -DskipTests

# Stage 2: Create the Final, Lightweight Runtime Image
# Use a smaller base image for security and size
FROM eclipse-temurin:17-jre-focal
# Set the port the application listens on
EXPOSE 8080
# Copy the built JAR from the 'build' stage
COPY --from=build /app/target/*.jar app.jar
# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]