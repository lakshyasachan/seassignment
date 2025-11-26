--- Build Stage: Compile the Java code ---

Use a JDK image that includes the Java Compiler (javac)

FROM eclipse-temurin:17-jdk-alpine AS build

Set the working directory inside the container

WORKDIR /app

Copy the Java source files (App and Test) to the container's working directory

COPY CalculatorApp.java .
COPY CalculatorTest.java .

Compile both Java files

This generates CalculatorApp.class and CalculatorTest.class

RUN javac CalculatorApp.java CalculatorTest.java

--- Runtime Stage: Create the final, smaller runtime image ---

Use a lightweight JRE (Java Runtime Environment) for the final image

FROM eclipse-temurin:17-jre-alpine

Set the working directory for the application

WORKDIR /usr/app

Copy the compiled class files from the 'build' stage

We only need the application class for runtime

COPY --from=build /app/CalculatorApp.class .

Define the entry point for the application

This command runs the compiled CalculatorApp class file

ENTRYPOINT ["java", "CalculatorApp"]
