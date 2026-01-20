# Stage 1: Build the app
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven descriptor
COPY pom.xml .

# Copy your folders exactly as they are
COPY main ./main
COPY test ./test
COPY resources ./resources

# Build the app
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR from stage 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
