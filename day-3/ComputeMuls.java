//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.util.regex.Pattern;
import java.io.IOException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.nio.file.Files;
import java.nio.file.Paths;


@Command(name = "ComputeMuls", mixinStandardHelpOptions = true, version = "1.0",
         description = "Extracts and computes the result of valid mul(X,Y) operations from corrupted strings.")
public class ComputeMuls implements Runnable {

    @Option(names = {"-f", "--file"}, description = "The file path for the reactor level reports.", defaultValue="./input.txt", required = true)
    private String filePath;

    Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    public static void main(String... args) {
        new CommandLine(new ComputeMuls()).execute(args);
    }

    @Override
    public void run() {
        try {
            var content = Files.readString(Paths.get(filePath));
            var sum = computeMulResults(content);
            System.out.println("Total sum of all valid mul operations: " + sum);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private int computeMulResults(String data) {
        var matcher = pattern.matcher(data);
        int sum = 0;

        while (matcher.find()) {
            sum += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));;
        }

        return sum;
    }
}
