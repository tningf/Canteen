# Stage 1: Build the application
# Start with a Maven image that includes OpenJDK 21
FROM maven:3.9.9-amazoncorretto-21 AS build

# Copy the source code into the /app directory
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Create the final image
# Start with a Java image that includes OpenJDK 21
FROM amazoncorretto:21.0.5

# Copy the JAR file from the build stage
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

