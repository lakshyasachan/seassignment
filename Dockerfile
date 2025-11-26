# --- Build Stage: Compile the Java code ---
FROM openjdk:17 AS builder

# Copy Java source files
COPY CalculatorApp.java CalculatorTest.java /app/

# Set working directory
WORKDIR /app

# Compile the application
RUN javac CalculatorApp.java CalculatorTest.java

# --- Runtime Stage: Create final lightweight image ---
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy compiled class files from builder stage
COPY --from=builder /app/*.class /app/

# Specify default command
CMD ["java", "CalculatorApp"]
