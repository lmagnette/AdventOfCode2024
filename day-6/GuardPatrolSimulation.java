//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name = "GuardPatrolSimulation", mixinStandardHelpOptions = true, version = "1.0",
         description = "Simulates a guard's patrol based on input grid and reports the number of distinct positions visited.")
public class GuardPatrolSimulation implements Runnable {

    @Option(names = {"-f", "--file"}, description = "Path to the file containing the grid.", defaultValue = "input.txt")
    private Path filePath;

    public static void main(String... args) {
        new CommandLine(new GuardPatrolSimulation()).execute(args);
    }

    @Override
    public void run() {
        try {
            List<String> gridAsList = Files.readAllLines(filePath);
            char[][] grid = gridAsList.stream().map(String::toCharArray).toArray(char[][]::new);
            var guardLocation = getStartingPoint(grid);
            var count = simulateGuardPath(grid, guardLocation);
            System.out.println("Distinct positions visited: " + count);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private Coordinate getStartingPoint(char[][] grid){
        for( var i = 0; i< grid.length; i++){
            for(var j = 0; j< grid[i].length; j++){
                if(grid[i][j] == '^'){
                    return new Coordinate(i, j, Direction.UP);
                }
            }
        }
        throw new RuntimeException("Cannot find the guard");
    }


    private int simulateGuardPath(char[][] grid, Coordinate guardLocation) {
        var visited = new HashSet<String>();
        visited.add(guardLocation.x()+"-"+guardLocation.y());
        var nextStep = guardLocation.nextStep();
        var step = guardLocation; 

        while(!isOutOfBound(grid, nextStep)){ 
            if(grid[nextStep.x()][nextStep.y()] =='#'){ // check for obstacle
                step = step.rotate();
            }else{
                step = nextStep;
                visited.add(step.x()+"-"+step.y());
            }
            nextStep = step.nextStep();
        }

        return visited.size();
    }

    private boolean isOutOfBound(char[][] grid, Coordinate location){
        return location.x() < 0 || location.y() < 0 
            || location.x() >= grid.length || location.y() >= grid[location.x()].length;
    }

    record Coordinate(int x, int y, Direction direction){

        public Coordinate rotate(){
            return new Coordinate(x, y, direction.rotateRight());
        }

        public Coordinate nextStep(){
            return switch (direction) {
                case UP -> new Coordinate(x-1, y, Direction.UP);
                case LEFT -> new Coordinate(x, y-1, Direction.LEFT);
                case DOWN -> new Coordinate(x+1, y, Direction.DOWN);
                case RIGHT -> new Coordinate(x, y+1, Direction.RIGHT);
            };
        }
    };
    enum Direction{
        UP,RIGHT,DOWN,LEFT;

        Direction rotateRight() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }
}
