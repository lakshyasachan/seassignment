# --- Build Stage: Compile the Java code ---
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Java source files
COPY CalculatorApp.java CalculatorTest.java /app/

# Compile Java sources
RUN javac CalculatorApp.java CalculatorTest.java

# --- Runtime Stage: Lightweight Image ---
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/*.class /app/

CMD ["java", "CalculatorApp"]
