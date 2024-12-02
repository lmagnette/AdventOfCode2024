//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "HistorianHysteriaPartTwo", mixinStandardHelpOptions = true, version = "1.0",
         description = "Calculates the similarity score between two lists of historically significant location IDs.")
public class HistorianHysteriaPartTwo implements Runnable {

    @Option(names = {"-f", "--file"}, description = "The file path for the list of location ID pairs.", defaultValue="input.txt", required = true)
    private String filePath;

    public static void main(String... args) {
        new CommandLine(new HistorianHysteriaPartTwo()).execute(args);
    }

    @Override
    public void run() {
        try {
            // Parse the file and separate into two lists
            var inputs = parseFile(filePath);
            var leftList = inputs.stream().map( v -> v.get(0)).sorted().toList();
            var rightList = inputs.stream().map(v -> v.get(1)).sorted().toList();

            // Calculate frequency of each number in the right list
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int number : rightList) {
                frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
            }

            // Calculate the similarity score
            int similarityScore = 0;
            for (int number : leftList) {
                int frequency = frequencyMap.getOrDefault(number, 0);
                similarityScore += number * frequency;
            }

            System.out.println("The similarity score between the lists is: " + similarityScore);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private List<List<Integer>> parseFile(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.map(String::trim)
                 .map(line -> line.split("\\s+"))
                 .filter(parts -> parts.length == 2 )
                 .map(parts -> List.of(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]))).toList();
        
        }
    }
}
