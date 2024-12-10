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
public class DiskFragmenterV2 implements Runnable {

    @Parameters(index = "0", description = "The file containing the disk map.", defaultValue = "input.txt")
    private String filename;

    private static final int EMPTY = -1;

    public static void main(String[] args) {
        new CommandLine(new DiskFragmenterV2()).execute(args);
    }

    @Override
    public void run() {
        try {
            String content = Files.readString(Paths.get(filename));
            List<Block> disk = initializeDisk(content);
            var compactedDisk = compactDisk(disk);
            System.out.println("Checksum: " + calculateChecksum(compactedDisk)); // 6239783302560
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private List<Block> initializeDisk(String content) {
        List<Block> disk = new ArrayList<>();
        boolean isSpace = false;
        int id = 0;
        for (String character : content.split("")) {
            int num = Integer.parseInt(character);
            disk.add(new Block(num, isSpace ? EMPTY : id));
            if (!isSpace) id++;
            isSpace = !isSpace;
        }
        return disk;
    }

    private List<Block> compactDisk(List<Block> diskMap) {
        var disk = new ArrayList<>(diskMap);
        int diskPlace = disk.size() - 1;
        while (diskPlace > 0) {
            Block current = disk.get(diskPlace);
            if (current.id() != EMPTY) {
                diskPlace = relocateBlock(disk, diskPlace);
            } else {
                diskPlace--;
            }
        }
        return disk;
    }

    private int relocateBlock(List<Block> disk, int index) {
        for (int i = 0; i < index; i++) {
            Block space = disk.get(i);
            if (space.id() == EMPTY) {
                Block block = disk.get(index);
                List<Block> blocks = space.fit(block);
                if (blocks != null) {
                    replaceBlocks(disk, i, index, blocks);
                    return disk.size() - 1;
                }
            }
        }
        return index - 1;
    }

    private void replaceBlocks(List<Block> disk, int spaceIndex, int blockIndex, List<Block> blocks) {
        disk.set(blockIndex, new Block(disk.get(blockIndex).size(), EMPTY));
        disk.remove(spaceIndex);
        disk.addAll(spaceIndex, blocks);
    }

    private BigInteger calculateChecksum(List<Block> disk) {
        BigInteger checksum = BigInteger.ZERO;
        int position = 0;
        for (Block block : disk) {
            for (int i = 0; i < block.size(); i++) {
                if (block.id() != EMPTY) {
                    checksum = checksum.add(BigInteger.valueOf(position).multiply(BigInteger.valueOf(block.id())));
                }
                position++;
            }
        }
        return checksum;
    }

    public record Block(int size, int id) {
        public List<Block> fit(Block work) {
            if (work.size > this.size) {
                return null;
            }
            List<Block> newList = new ArrayList<>();
            newList.add(work);
            if (work.size < this.size) {
                newList.add(new Block(this.size - work.size, EMPTY));
            }
            return newList;
        }
    }
}