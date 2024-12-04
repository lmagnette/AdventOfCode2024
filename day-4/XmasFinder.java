//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import java.io.IOException;
import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.nio.file.Files;
import java.nio.file.Paths;


@Command(name = "XmasFinder", mixinStandardHelpOptions = true, version = "1.0",
         description = "Finds all occurrences of the word 'XMAS' in all directions in a given word search grid.")
public class XmasFinder implements Runnable {

    @Option(names = {"-f", "--file"}, description = "File path containing the word search grid.", defaultValue="input.txt", required = true)
    private String filePath;

    public static void main(String... args) {
        new CommandLine(new XmasFinder()).execute(args);
    }

    @Override
    public void run() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            char[][] grid = lines.stream()
                                 .map(String::toCharArray)
                                 .toArray(char[][]::new);
            int count = countXmas(grid);
            System.out.println("The word 'XMAS' appears " + count + " times in the grid.");
        } catch (IOException e) {
            System.err.println("Failed to read the file: " + e.getMessage());
        }
    }

    private int countXmas(char[][] grid) {
        int total = 0;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        String target = "XMAS";

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                for (int dir = 0; dir < dr.length; dir++) {
                    if (checkDirection(grid, row, col, dr[dir], dc[dir], target)) {
                        total++;
                    }
                }
            }
        }

        return total;
    }

    private boolean checkDirection(char[][] grid, int startRow, int startCol, int dr, int dc, String target) {
        int n = grid.length;
        int m = grid[0].length;
        for (int i = 0; i < target.length(); i++) {
            int rr = startRow + i * dr;
            int cc = startCol + i * dc;
            if (rr < 0 || rr >= n || cc < 0 || cc >= m || grid[rr][cc] != target.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}