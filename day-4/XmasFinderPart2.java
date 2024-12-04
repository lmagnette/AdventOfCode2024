//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "XMASFinder", mixinStandardHelpOptions = true, version = "1.0",
         description = "Finds all occurrences of the word 'MAS' in an X pattern in a given word search grid.")
public class XmasFinderPart2 implements Runnable {

    @Option(names = {"-f", "--file"}, description = "File path containing the word search grid.", defaultValue="input.txt",  required = true)
    private String filePath;

    public static void main(String... args) {
        new CommandLine(new XmasFinderPart2()).execute(args);
    }

    @Override
    public void run() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);
            System.out.println("The pattern 'X-MAS' appears " + countXMAS(grid) + " times in the grid.");
        } catch (IOException e) {
            System.err.println("Failed to read the file: " + e.getMessage());
        }
    }


    private long countXMAS(char[][] grid) {
        List<Coordinate> potentialCenters = new ArrayList<>();
        for (int i = 1; i < grid.length-1; i++) { // exclude edges on x
            for (int j = 1; j < grid[i].length-1; j++) { // exclude edges on y
                if (grid[i][j] == 'A') {
                    potentialCenters.add(new Coordinate(i, j));
                }
            }
        }

        return potentialCenters.stream().filter(c -> checkLeftDiagonal(grid, c) && checkRightDiagonal(grid, c))
                                    .count();
    }

    private boolean checkLeftDiagonal(char[][] grid, Coordinate c){
        var topLeft = grid[c.x-1][c.y-1];
        var bottomRight = grid[c.x+1][c.y+1];
        if(topLeft == 'M' && bottomRight == 'S')return true;
        if(topLeft == 'S' && bottomRight == 'M')return true;
        return false;
    }
    private boolean checkRightDiagonal(char[][] grid, Coordinate c){
        var topRight = grid[c.x-1][c.y+1];
        var bottomLeft = grid[c.x+1][c.y-1];
        if(topRight == 'M' && bottomLeft == 'S')return true; 
        if(topRight == 'S' && bottomLeft == 'M')return true;
        return false;
    }

    record Coordinate(int x, int y){}
}