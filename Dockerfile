# Use an official Maven image as a parent image
FROM maven:latest

# Set metadata information
LABEL authors="mikt"

# Set the working directory in the container
WORKDIR /app

# Copy the entire project to the container
COPY . /app/


EXPOSE 8000
# Package your application
RUN mvn package

# Run the main class (assuming your application has a main class)
CMD ["java", "-jar", "target/OFOS-1.0-SNAPSHOT.jar"]