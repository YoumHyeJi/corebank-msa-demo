# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download and install Gradle
# RUN ./gradlew --version

# Copy project files and build
COPY . /app
RUN ./gradlew bootJar -x test

# Stage 2: Use a smaller OpenJDK runtime image for the final image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]