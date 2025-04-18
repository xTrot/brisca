# Use a base image with Java installed
FROM openjdk:25-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
ADD target/brisca-*-shaded.jar /app/brisca.jar

# Expose the port your app runs on (if necessary)
EXPOSE 80

# Command to run the application
CMD ["java", "-jar", "/app/brisca.jar"]