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

@Command(name = "ReorderPrintQueue", mixinStandardHelpOptions = true, version = "1.0",
         description = "Reorders incorrectly ordered updates and calculates the sum of their middle pages.")
public class ReorderPrintQueue implements Runnable {

    @Option(names = {"-f", "--file"}, description = "File path containing the rules and updates.", defaultValue="input.txt", required = true)
    private String filePath;

    public static void main(String... args) {
        new CommandLine(new ReorderPrintQueue()).execute(args);
    }

    @Overridegit 
    public void run() {
        try {
            var content = Files.readString(Path.of(filePath));
            var sections = content.split(System.lineSeparator()+System.lineSeparator(), 2);
            var rulesLines = List.of(sections[0].split(System.lineSeparator()));
            var updatesLines = List.of(sections[1].split(System.lineSeparator()));

            Map<Integer, Set<Integer>> constraints = parseConstraints(rulesLines);

            var sumOfMiddlePages = updatesLines.stream()
                        .map(this::lineToList)
                        .filter(l -> !isUpdateValid(l, constraints))
                        .mapToInt(l -> getMiddleIndex(l,constraints))
                        .sum();

            System.out.println("Sum of middle pages from corrected updates: " + sumOfMiddlePages);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private int getMiddleIndex(List<Integer> list,  Map<Integer, Set<Integer>> constraints){
        var orderedUpdate = topologicalSort(list, constraints);
        return orderedUpdate.get(orderedUpdate.size() / 2);
    }

    private List<Integer> lineToList(String line){
        return Arrays.stream(line.split(","))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
    }

    private Map<Integer, Set<Integer>> parseConstraints(List<String> rules) {
        return rules.stream()
                    .map(rule -> rule.split("\\|"))
                    .collect(Collectors.toMap(parts -> Integer.parseInt(parts[0]),
                                              parts -> new HashSet<>(Collections.singleton(Integer.parseInt(parts[1]))),
                                              (existing, addition) -> { existing.addAll(addition); return existing; }));
    }
    private boolean isUpdateValid(List<Integer> update, Map<Integer, Set<Integer>> constraints) {
        var pageIndex = update.stream()
                              .collect(Collectors.toMap(Function.identity(), update::indexOf));
        return update.stream()
                     .allMatch(page -> constraints.getOrDefault(page, Set.of())
                     .stream()
                     .allMatch(mustFollow -> pageIndex.getOrDefault(page,0) < pageIndex.getOrDefault(mustFollow, Integer.MAX_VALUE)));
    }

    private List<Integer> topologicalSort(List<Integer> pages, Map<Integer, Set<Integer>> constraints) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int page : pages) {
            graph.put(page, new HashSet<>());
            inDegree.put(page, 0);
        }

        // Build the graph
        for (int page : pages) {
            if (constraints.containsKey(page)) {
                for (int mustFollow : constraints.get(page)) {
                    if (pages.contains(mustFollow)) {
                        graph.get(page).add(mustFollow);
                        inDegree.put(mustFollow, inDegree.get(mustFollow) + 1);
                    }
                }
            }
        }

        // Topological sort using Kahn's algorithm
        Queue<Integer> queue = new LinkedList<>();
        for (int page : pages) {
            if (inDegree.get(page) == 0) {
                queue.add(page);
            }
        }

        List<Integer> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            int page = queue.poll();
            sorted.add(page);
            for (int neighbor : graph.get(page)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }
        return sorted;

    }
}