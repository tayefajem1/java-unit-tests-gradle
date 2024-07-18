# Use Maven as the base image
FROM gradle:7.2-jdk11

# Set the working directory inside the container
WORKDIR /app

COPY ./target/Enlistment-1.0-SNAPSHOT.jar /app/

# Command to run the Java application
CMD ["java", "-jar", "target/Enlistment-1.0-SNAPSHOT.jar"]