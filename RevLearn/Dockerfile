
# Stage 1: build app
FROM maven:3.8.4-openjdk-17-slim AS build

# set up workdir
WORKDIR /app

# install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy over our project's src code.
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Running the Application
FROM eclipse-temurin:17-jdk AS run

# Also set this new Stage's working directory
WORKDIR /app

# Bring over the JAR file from the build Stage.
COPY --from=build /app/target/RevLearn-0.0.1-SNAPSHOT.jar ./app.jar

# Tell Docker to use port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# How to run
# 1. docker build --tag=revlearn:latest .
# 2. docker run -p 8080:8080 revlearn:latest
