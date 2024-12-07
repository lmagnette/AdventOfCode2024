//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "BridgeRepair", mixinStandardHelpOptions = true, version = "1.3",
         description = "Determine which equations could possibly be true using +, *, and || operators.")
public class BridgeRepairPart2 implements Runnable {

    @Option(names = {"-f", "--file"}, description = "Path to the file containing the equations.", defaultValue = "input.txt")
    private Path filePath;

    public static void main(String... args) {
        new CommandLine(new BridgeRepairPart2()).execute(args);
    }

    @Override
    public void run() {
        try {
            var lines = Files.readAllLines(filePath);
            var totalCalibrationResult = lines.stream()
                                              .map(this::parseLine)
                                              .filter(Objects::nonNull)
                                              .filter(this::canAchieveTarget)
                                              .mapToLong(Equation::target)
                                              .sum();

            System.out.println("Total calibration result: " + totalCalibrationResult);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private Equation parseLine(String line) {
        try {
            var parts = line.split(":");
            var target = Long.parseLong(parts[0].trim());
            var numbers = Arrays.stream(parts[1].trim().split("\\s+"))
                                .mapToLong(Long::parseLong)
                                .toArray();
            return new Equation(target, numbers);
        } catch (Exception e) {
            System.err.println("Invalid line format: " + line);
            return null;
        }
    }

    private boolean canAchieveTarget(Equation equation) {
        if (equation.numbers.length < 2) return false;

        var numOperators = equation.numbers.length - 1;
        var maxCombinations = (int) Math.pow(3, numOperators); // 3^(numOperators) combinations

        for (int operatorMask = 0; operatorMask < maxCombinations; operatorMask++) {
            if (evaluateExpression(equation.numbers, operatorMask) == equation.target) {
                return true;
            }
        }

        return false;
    }

    private long evaluateExpression(long[] numbers, int operatorMask) {
        long result = numbers[0];
        for (int i = 0; i < numbers.length - 1; i++) {
            int operator = (operatorMask / (int) Math.pow(3, i)) % 3; // Extract operator at position i
            switch (operator) {
                case 0 -> result += numbers[i + 1]; // Addition
                case 1 -> result *= numbers[i + 1]; // Multiplication
                case 2 -> result = concatenate(result, numbers[i + 1]); // Concatenation
            }
        }
        return result;
    }

    private long concatenate(long left, long right) {
        String concatenated = String.valueOf(left) + String.valueOf(right);
        return Long.parseLong(concatenated);
    }

    record Equation(long target, long[] numbers) {
        @Override
        public String toString() {
            return target + ": " + Arrays.toString(numbers);
        }
    }
}