//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ReactorSafetyCheckWithDampener", mixinStandardHelpOptions = true, version = "1.0",
         description = "Checks safety of reactor level reports, allowing for one removable bad level.")
public class ReactorSafetyCheckWithDampener implements Runnable {

    @Option(names = {"-f", "--file"}, description = "The file path for the reactor level reports.", defaultValue="./input.txt", required = true)
    private String filePath;

    public static void main(String... args) {
        new CommandLine(new ReactorSafetyCheckWithDampener()).execute(args);
    }

    @Override
    public void run() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            var safeReports = lines.stream()
                                    .map(this::lineToIntegers)
                                    .filter(this::canBeMadeSafe)
                                    .count();

            System.out.println("Number of safe reports: " + safeReports);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private List<Integer> lineToIntegers(String line){
        return Arrays.stream(line.trim().split("\\s+"))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    }

    private boolean canBeMadeSafe(List<Integer> levels) {
        if (isMonotonicallyIncreasing(levels) || isMonotonicallyDecreasing(levels)) {
            return true;
        }

        // Check if removing any one level results in a safe report
        for (int i = 0; i < levels.size(); i++) {
            var modifiedLevels = new ArrayList<>(levels);
            modifiedLevels.remove(i);
            if (isMonotonicallyIncreasing(modifiedLevels) || isMonotonicallyDecreasing(modifiedLevels)) {
                return true;
            }
        }

        return false;
    }

    private boolean isMonotonicallyIncreasing(List<Integer> levels) {
        for (int i = 0; i < levels.size() - 1; i++) {
            int diff = levels.get(i + 1) - levels.get(i);
            if (diff < 1 || diff > 3) {
                return false;
            }
        }
        return true;
    }

    private boolean isMonotonicallyDecreasing(List<Integer> levels) {
        for (int i = 0; i < levels.size() - 1; i++) {
            int diff = levels.get(i) - levels.get(i + 1);
            if (diff < 1 || diff > 3) {
                return false;
            }
        }
        return true;
    }
}