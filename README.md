# Advent of Code 2024 Solutions (Java & JBang)

## Repository Overview

This repository contains solutions to the [Advent of Code 2024](https://adventofcode.com/2024) challenges, written in Java and designed to be executed with JBang. Each script tackles a specific puzzle from the event, utilizing Picocli for command-line argument parsing to enhance functionality and user interaction.

## Technologies Used

- Java
- JBang: Used to easily run Java programs as scripts without the need for manual compilation.
- Picocli: Provides a framework for building command-line applications with advanced parsing capabilities and generated help messages.

## Prerequisites

## Before running the scripts, ensure you have the following installed:

- Java JDK 21 or later: Required to run Java applications.
- JBang: Simplifies running Java scripts. 

## Scripts in This Repository

Below is a brief overview of each script and its purpose:

1. [Day 1](https://adventofcode.com/2024/day/1) 
     - Historian Hysteria: Analyzes historical location data to determine possible locations of the missing Chief Historian.
     - Historian Hysteria part 2: Evaluates how often each number from the left list appears in the right list and computes a total similarity score. This considers potential misinterpretations or errors in recording the location IDs.
2. [Day 2](https://adventofcode.com/2024/day/2)
    - Reactor Safety Check: Assesses safety reports from the Red-Nosed Reindeer nuclear plant for compliance with safety standards.
    - Reactor Safety Check With Dampener: Extends the Day 2 safety check to include a tolerance for one faulty level per report, simulating a problem dampener device.
3. [Day 3](https://adventofcode.com/2024/day/3)
    - Mull It Over: Identify and compute the results of valid multiplication instructions from a corrupted string of data.
    - Mull It Over With Conditionals: Extend the computation to include conditional statements that enable or disable multiplication instructions based on their sequence in the corrupted data.  
4. [Day 4](https://adventofcode.com/2024/day/4)
    - Comprehensive XMAS Word Search: Count all instances of the word “XMAS” in all directions in a word search grid, including horizontally, vertically, and diagonally, both forwards and backwards.
    - Complex X-MAS Pattern Detection: Identify and count all instances of the pattern “X-MAS”, where “MAS” forms an X shape around a central ‘A’, considering all diagonal directions and allowing for overlap, in a word search grid.   
 5. [Day 5](https://adventofcode.com/2024/day/5)    
    - Validate Page Order: Verify if page updates are in the correct order based on predefined page sequencing rules, and sum the middle page numbers of valid sequences.
    -  Reorder and Analyze Updates: Identify incorrectly ordered page updates, reorder them according to specified rules using topological sorting, and calculate the sum of middle page numbers from the reordered updates.

## Running the Scripts

To run any script in this repository, use the following general command:

jbang <script-name>.java --file path/to/your/input.txt

Replace <script-name> with the actual script file name and path/to/your/input.txt with the path to your input data file.

## Adding New Solutions

To add a new solution:

1.	Create a new Java file using the naming convention DayX_<PuzzleName>.java.
2.	Implement the solution using Java and, optionally, Picocli for argument parsing.
3.	Update this README with the new script’s details under “Scripts in This Repository.”

## Contributing

Contributions to this repository are welcome. Please ensure your pull requests are well-documented and tested. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
