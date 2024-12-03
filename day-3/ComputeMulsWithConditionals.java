//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ComputeMulsWithConditionals", mixinStandardHelpOptions = true, version = "1.0",
         description = "Reads a file and computes the result of mul(X,Y) operations from corrupted strings, considering conditional statements.")
public class ComputeMulsWithConditionals implements Runnable {
    private static final String REGEX_MUL = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)";

    @Option(names = {"-f", "--file"}, description = "File path containing input data.", defaultValue = "./input.txt", required = true)
    private String filePath;
    
    private Pattern pattern = Pattern.compile(REGEX_MUL);

    public static void main(String... args) {
        new CommandLine(new ComputeMulsWithConditionals()).execute(args);
    }

    @Override
    public void run() {
        try {
            var content = Files.readString(Paths.get(filePath));
            var sum = computeMulResults(content);
            System.out.println("Total sum of all valid mul operations: " + sum );
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private int computeMulResults(String data) {
        var matcher = pattern.matcher(data);
        var sum = 0;
        var enableMul = true;

        while (matcher.find()) {
            switch (matcher.group()) {
                case "do()" -> enableMul = true;
                case "don't()" -> enableMul = false;
                default -> sum += computeMul(enableMul, matcher);
            }
        }

        return sum;
    }

    private int computeMul(boolean enableMul, Matcher matcher){
        if(enableMul && matcher.group(1) != null && matcher.group(2) != null){
            return Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }
        return 0;
    }
}