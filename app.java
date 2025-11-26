package com.example;

import java.util.Scanner;

/**
 * Simple Calculator Command Line Interface (CLI) Application.
 * This application takes two numbers and an operator, and returns the result.
 * It is structured to be easily built and tested by Jenkins.
 */
public class CalculatorApp {

    public static void main(String[] args) {
        System.out.println("--- Simple CLI Calculator ---");
        
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();

            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();

            System.out.print("Enter operator (+, -, *, /): ");
            String operator = scanner.next();

            double result = calculate(num1, num2, operator);
            System.out.println("Result: " + num1 + " " + operator + " " + num2 + " = " + result);

        } catch (Exception e) {
            System.err.println("Error: Invalid input or operation. " + e.getMessage());
        }
    }

    /**
     * Performs the calculation based on the given numbers and operator.
     * @param num1 The first number.
     * @param num2 The second number.
     * @param operator The arithmetic operator string.
     * @return The result of the operation.
     * @throws IllegalArgumentException if the operator is invalid or division by zero occurs.
     */
    public static double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    throw new IllegalArgumentException("Cannot divide by zero.");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
