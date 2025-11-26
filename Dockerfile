Use a lightweight JDK image for building the Java application (Build Stage)

FROM maven:3.8.7-eclipse-temurin-17 AS build

Set the working directory inside the container

WORKDIR /app

Copy the pom.xml file first to download dependencies (improves cache performance)

COPY pom.xml .

Download dependencies

RUN mvn dependency:go-offline

Copy the rest of the application code

COPY src ./src

Build the final JAR file

The application JAR will be located in the 'target' directory

RUN mvn package -DskipTests

--- Second Stage: Create the final, smaller runtime image ---

Use a lightweight JRE (Java Runtime Environment) for the final image

FROM eclipse-temurin:17-jre-alpine

Set the working directory

WORKDIR /usr/app

Copy the built JAR file from the 'build' stage

The name of the JAR file will depend on the pom.xml artifactId (usually target/*.jar)

Assuming the JAR is named 'calculator-cli-app.jar' for simplicity.

Check your pom.xml for the exact name if this fails, or use '*.jar'

COPY --from=build /app/target/*.jar calculator-cli-app.jar

Define the entry point for the application

This is what runs when 'docker run' is executed

ENTRYPOINT ["java", "-jar", "calculator-cli-app.jar"]
