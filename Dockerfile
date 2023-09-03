# Use Gradle as the base image
FROM gradle:7.2-jdk11

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle /app/

# Download Gradle dependencies to cache them
RUN gradle build --no-daemon

# Copy the source code into the container
COPY src /app/src

# Build the Java application using Gradle
RUN gradle build --no-daemon

# Command to run the Java application
CMD ["java", "-jar", "build/libs/Enlistment-1.0-SNAPSHOT.jar"]
