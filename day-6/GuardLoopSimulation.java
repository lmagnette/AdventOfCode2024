//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "GuardLoopSimulation", mixinStandardHelpOptions = true, version = "1.0",
         description = "Find positions to place an obstruction so that the guard gets stuck in a loop.")
public class GuardLoopSimulation implements Runnable {

    @Option(names = {"-f", "--file"}, description = "Path to the file containing the grid.", defaultValue = "input.txt")
    private Path filePath;

    public static void main(String... args) {
        new CommandLine(new GuardLoopSimulation()).execute(args);
    }

    @Override
    public void run() {
        try {
            var gridAsList = Files.readAllLines(filePath);
            var grid = gridAsList.stream().map(String::toCharArray).toArray(char[][]::new);
            var count = findPossibleLoops(grid);
            System.out.println("Distinct possible loops: " + count);
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

    private int findPossibleLoops(char[][] grid){
        var guardLocation = getStartingPoint(grid);
        var count = 0;
        for (var i = 0; i < grid.length; i++){
            for(var j = 0; j < grid[i].length; j++){
                
                if (guardLocation.x() == i && guardLocation.y() == j) continue;
                if (grid[i][j] == '#') continue;

                var original = grid[i][j]; // Temporarily place obstruction
                grid[i][j] = '#';
                if (simulateGuardPath(grid, guardLocation)) {
                    count++;
                }
               
                grid[i][j] = original; // Revert             
            }
        }
        return count;
    }


    private boolean simulateGuardPath(char[][] grid, Coordinate guardLocation) {
        var visited = new HashSet<String>();
        var nextStep = guardLocation.nextStep();
        var step = guardLocation;

        while(!isOutOfBound(grid, nextStep)){ 
            if(grid[nextStep.x()][nextStep.y()] =='#'){ // check for obstacle
                step = step.rotate();
            }else{
                step = nextStep;
                if(!visited.add(step.toString())) return true;
            }
            nextStep = step.nextStep();
        }

        return false;
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