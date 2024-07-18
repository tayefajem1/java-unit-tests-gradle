# Use Maven as the base image
FROM gradle:7.2-jdk11

# Set the working directory inside the container
WORKDIR /app

COPY ./target/splitwise-1.0.jar /app/

# Command to run the Java application
CMD ["java", "-jar", "target/splitwise-1.0.jar"]