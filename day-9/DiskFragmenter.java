//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.math.BigInteger;

@Command(name = "DiskFragmenter", mixinStandardHelpOptions = true, version = "DiskFragmenter 1.0",
        description = "Compacts disk blocks based on a dense map format and calculates checksum.")
public class DiskFragmenter implements Runnable {

    @Parameters(index = "0", description = "The file containing the disk map.", defaultValue = "input.txt")
    private String filename;

    public static void main(String[] args) {
        new CommandLine(new DiskFragmenter()).execute(args);
    }

    @Override
    public void run() {
        try {
            String content = Files.readString(Paths.get(filename));
            var diskMap = parseDiskMap(content);
            var disk = compressDisk(diskMap);
            var checksum = calculateChecksum(disk);
            System.out.println("Final checksum: " + checksum);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    private List<Integer> parseDiskMap(String diskMap){
        List<Integer> disk = new ArrayList<>();
        boolean space = false;
        int id = 0;
        for (String character : diskMap.split("")) {
            int num = Integer.parseInt(character);

            for (int i = 0; i < num; i++) disk.add(space? -1 : id);
            if(!space) id++;
    
            space = !space;
        }
        return disk;
    }

    private List<Integer> compressDisk(List<Integer> diskSpace){
        var disk = new ArrayList<>(diskSpace);
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i) == -1) {
                int value = -1;
                while (value == -1) {
                    value = disk.remove(disk.size() - 1);
                }
                if (disk.size() <= i) {
                    disk.add(value);
                    break;
                }
                disk.remove(i);
                disk.add(i, value);
            }
        }
        return disk;
    }


    private BigInteger calculateChecksum(List<Integer> disk) {
        BigInteger count = BigInteger.ZERO;
        for (int i = 0; i < disk.size(); i++) {
            count = count.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(disk.get(i))));
        }
        return count;
    }
}