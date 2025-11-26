/**
 * Simple Test class to verify core calculation logic of CalculatorApp.
 * * This file is part of a non-Maven CI/CD pipeline, so it uses direct 
 * method calls and System.exit() to signal success or failure to the 
 * Jenkins build process.
 */
public class CalculatorTest {

    public static void main(String[] args) {
        System.out.println("--- Starting Simple Calculator Functional Tests ---");

        // Test 1: Addition (2 + 3 = 5)
        runTest(2, 3, "+", 5.0, "Addition");

        // Test 2: Subtraction (10 - 4 = 6)
        runTest(10, 4, "-", 6.0, "Subtraction");
        
        // Test 3: Multiplication (5 * 4 = 20)
        runTest(5, 4, "*", 20.0, "Multiplication");
        
        // Test 4: Division (10 / 2 = 5)
        runTest(10, 2, "/", 5.0, "Division");

        // Test 5: Division by Zero Check (CRITICAL TEST)
        try {
            // Attempt to calculate 5 / 0
            CalculatorApp.calculate(5, 0, "/");
            // If we reach this line, the test failed because no exception was thrown
            System.err.println("TEST FAILED: Division by zero did not throw an error.");
            System.exit(1); // Exit with failure code
        } catch (IllegalArgumentException e) {
            // Success: Expected behavior is to catch the IllegalArgumentException
            System.out.println("TEST PASSED: Division by zero check successful (caught expected error).");
        } catch (Exception e) {
             // Failure: Caught an unexpected exception type
             System.err.println("TEST FAILED: Unexpected error during division by zero: " + e.getMessage());
             System.exit(1); 
        }

        System.out.println("--- All Calculator Functional Tests Passed ---");
    }

    /**
     * Executes a single test case and compares the result to the expected value.
     */
    private static void runTest(double num1, double num2, String op, double expected, String name) {
        // Ensure CalculatorApp.java is in the same directory to be able to call this static method
        double result = CalculatorApp.calculate(num1, num2, op);
        if (result == expected) {
            System.out.println("TEST PASSED: " + name + " (" + num1 + op + num2 + ")");
        } else {
            System.err.println("TEST FAILED: " + name + ". Expected " + expected + ", got " + result);
            System.exit(1); // Exit with failure code to fail the Jenkins job
        }
    }
}
