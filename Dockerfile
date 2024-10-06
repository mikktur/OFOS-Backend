FROM maven:3.8.5-openjdk-21 AS build

WORKDIR /app

COPY pom.xml /app/

COPY src /app/src/

RUN mvn clean package

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar /app/OFOS-1.0-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "OFOS-1.0-SNAPSHOT.jar"]
