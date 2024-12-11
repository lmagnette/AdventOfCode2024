//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ResonantCollinearity", mixinStandardHelpOptions = true, version = "1.3",
         description = "Calculate the number of unique antinode locations.")
public class ResonantCollinearity implements Runnable {

    @Option(names = {"-f", "--file"}, description = "Path to the file containing the map.", defaultValue = "input.txt")
    private Path filePath;

    public static void main(String... args) {
        new CommandLine(new ResonantCollinearity()).execute(args);
    }

    @Override
    public void run() {
        try {
            var grid = Files.readAllLines(filePath);
            int height = grid.size();
            int width = grid.get(0).length();
            var antiPoints = getAntiPoints(getPoints(grid), width, height);
            System.out.println("Total unique antinode locations: "+antiPoints.size() );
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private Set<Point> getAntiPoints(Set<Point> points, int width, int height){
        return points.stream().flatMap(pointA -> points.stream()
                                 .filter(pointB -> !pointA.equals(pointB))
                                 .map(pointB -> pointA.getAntiPoint(pointB, width, height))
                                 .filter(newPoint -> newPoint != null && !points.contains(newPoint)))
        .collect(Collectors.toSet());
    }

    private Set<Point> getPoints(List<String> lines){
        return IntStream.range(0, lines.size())
                        .boxed()
                        .flatMap(y -> IntStream.range(0, lines.get(y).length())
                                               .mapToObj(x -> new Point(lines.get(y).charAt(x), x, y))
                                               .filter(p -> p.character() != '.'))
                        .collect(Collectors.toSet());
    }

    record Point(char character, int x, int y){
        private static final char ANTINODE = '#';

        public Point getAntiPoint(Point other, int width, int height) {
            if (this.character != other.character) {
                return null;
            }
            int deltaX = other.x - this.x;
            int nextX = other.x + deltaX;
            int deltaY = other.y - this.y;
            int nextY = other.y + deltaY;
    
            if (nextX < 0 || nextY < 0 || nextX >= width || nextY >= height) {
                return null;
            }
    
            return new Point(ANTINODE, nextX, nextY); 
        }

        public List<Point> getAntiPoints(Point other, int width, int height) {
            if (this.character != other.character) {
                return new ArrayList<>();
            }
    
            int deltaX = other.x - this.x;
            int deltaY = other.y - this.y;
    
            int nextX = other.x + deltaX;
            int nextY = other.y + deltaY;
    
            List<Point> newPoints = new ArrayList<>();
            while (nextX >= 0 && nextX < width && nextY >= 0 && nextY < height) {
                newPoints.add(new Point(ANTINODE, nextX, nextY)); 
                nextX += deltaX;
                nextY += deltaY;
            }
            return newPoints;
        }
    }
}