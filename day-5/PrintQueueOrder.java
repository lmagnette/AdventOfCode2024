//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "PrintQueueOrder", mixinStandardHelpOptions = true, version = "1.0",
         description = "Determines the correct order of page updates and calculates the sum of the middle pages.")
public class PrintQueueOrder implements Runnable {

    @Option(names = {"-f", "--file"}, description = "File path containing the rules and updates.", defaultValue="input.txt", required = true)
    private String filePath;

    public static void main(String... args) {
        new CommandLine(new PrintQueueOrder()).execute(args);
    }

    @Override
    public void run() {
        try {
            var content = Files.readString(Path.of(filePath));
            var sections = content.split(System.lineSeparator()+System.lineSeparator(), 2);
            var rulesLines = List.of(sections[0].split(System.lineSeparator()));
            var updatesLines = List.of(sections[1].split(System.lineSeparator()));

            var constraints = parseConstraints(rulesLines);
            var sumOfMiddlePages = updatesLines.stream()
                                               .map(this::parseUpdate)
                                               .filter(update -> isUpdateValid(update, constraints))
                                               .mapToInt(update -> update.get(update.size() / 2))
                                               .sum();

            System.out.println("Sum of middle pages from valid updates: " + sumOfMiddlePages);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private List<Integer> parseUpdate(String line) {
        return Arrays.stream(line.split(","))
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    private Map<Integer, Set<Integer>> parseConstraints(List<String> rules) {
        return rules.stream()
                    .map(rule -> Arrays.stream(rule.split("\\|")).mapToInt(Integer::parseInt).toArray())
                    .collect(Collectors.toMap(
                                parts -> parts[0],
                                parts -> new HashSet<>(Collections.singletonList(parts[1])),
                                (existing, addition) -> { existing.addAll(addition); return existing; }));
    }

    private boolean isUpdateValid(List<Integer> update, Map<Integer, Set<Integer>> constraints) {
        Map<Integer, Integer> pageIndex = update.stream()
                                                .collect(Collectors.toMap(Function.identity(), update::indexOf));
        return update.stream()
                     .allMatch(page -> constraints.getOrDefault(page, Collections.emptySet())
                     .stream()
                     .allMatch(mustFollow -> pageIndex.getOrDefault(page,0) < pageIndex.getOrDefault(mustFollow, Integer.MAX_VALUE)));
    }
}
