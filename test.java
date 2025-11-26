package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the core calculation logic in CalculatorApp.
 * These tests must run successfully for the pipeline to continue to the Docker stage.
 */
public class CalculatorTest {

    // Test case for addition
    @Test
    void testAddition() {
        assertEquals(5.0, CalculatorApp.calculate(2, 3, "+"), "2 + 3 should equal 5.0");
    }

    // Test case for subtraction
    @Test
    void testSubtraction() {
        assertEquals(-1.0, CalculatorApp.calculate(2, 3, "-"), "2 - 3 should equal -1.0");
    }

    // Test case for multiplication
    @Test
    void testMultiplication() {
        assertEquals(6.0, CalculatorApp.calculate(2, 3, "*"), "2 * 3 should equal 6.0");
    }

    // Test case for division
    @Test
    void testDivision() {
        assertEquals(4.0, CalculatorApp.calculate(8, 2, "/"), "8 / 2 should equal 4.0");
    }

    // Test case for division by zero (should throw an exception)
    @Test
    void testDivisionByZero() {
        // We expect an IllegalArgumentException when dividing by zero
        assertThrows(IllegalArgumentException.class, () -> {
            CalculatorApp.calculate(5, 0, "/");
        }, "Division by zero should throw IllegalArgumentException");
    }

    // Test case for invalid operator
    @Test
    void testInvalidOperator() {
        // We expect an IllegalArgumentException for an unknown operator
        assertThrows(IllegalArgumentException.class, () -> {
            CalculatorApp.calculate(5, 2, "%");
        }, "Invalid operator should throw IllegalArgumentException");
    }
}
