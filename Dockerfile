# Stage 1: Build the application
FROM gradle:7.2-jdk11 AS build
WORKDIR /app
COPY . .
RUN gradle build

# Stage 2: Create the runtime image
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/Enlistment-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "Enlistment-1.0-SNAPSHOT.jar"]
