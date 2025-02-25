package M3;
// UCID: mga46
// Date: February 24, 2025
// Summary: Solution for Task #1 - Implement a calculator using command-line arguments

/*
Challenge 1: Command-Line Calculator
------------------------------------
- Accept two numbers and an operator as command-line arguments
- Supports addition (+) and subtraction (-)
- Allow integer and floating-point numbers
- Ensures correct decimal places in output based on input (e.g., 0.1 + 0.2 → 1 decimal place)
- Display an error for invalid inputs or unsupported operators
- Capture 5 variations of tests
*/

public class CommandLineCalculator extends BaseClass {
    private static String ucid = "mga46"; // <-- change to your ucid

    public static void main(String[] args) {
        printHeader(ucid, 1, "Objective: Implement a calculator using command-line arguments.");

        if (args.length != 3) {
            System.out.println("Usage: java M3.CommandLineCalculator <num1> <operator> <num2>");
            printFooter(ucid, 1);
            return;
        }

        try {
            System.out.println("Calculating result...");
            // extract the equation (format is <num1> <operator> <num2>)
            String num1Str = args[0];
            String operator = args[1];
            String num2Str = args[2];
            // check if operator is addition or subtraction
            boolean isFloat = num1Str.contains(".") || num2Str.contains(".");
            // check the type of each number and choose appropriate parsing
            double num1 = isFloat ? Double.parseDouble(num1Str) : Integer.parseInt(num1Str);
            double num2 = isFloat ? Double.parseDouble(num2Str) : Integer.parseInt(num2Str);
            double result;

            // generate the equation result (Important: ensure decimals display as the
            // longest decimal passed)
            if (operator.equals("+")) {
                result = num1 + num2;
            } else if (operator.equals("-")) {
                result = num1 - num2;
            } else {
                System.out.println("Error: Unsupported operator. Use + or -.");
                printFooter(ucid, 1);
                return;
            } 
            String format = isFloat ? "%.1f%n" : "%.0f%n";
            System.out.printf("Result: " + format, result);
            // i.e., 0.1 + 0.2 would show as one decimal place (0.3), 0.11 + 0.2 would shows
            // as two (0.31), etc

        } catch (Exception e) {
            System.out.println("Invalid input. Please ensure correct format and valid numbers.");
        }

        printFooter(ucid, 1);
    }
}
