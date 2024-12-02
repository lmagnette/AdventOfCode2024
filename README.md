# Advent of Code 2024 Solutions (Java & JBang)

## Repository Overview

This repository contains solutions to the Advent of Code 2024 challenges, written in Java and designed to be executed with JBang. Each script tackles a specific puzzle from the event, utilizing Picocli for command-line argument parsing to enhance functionality and user interaction.

## Technologies Used

	•	Java
	•	JBang: Used to easily run Java programs as scripts without the need for manual compilation.
	•	Picocli: Provides a framework for building command-line applications with advanced parsing capabilities and generated help messages.

## Prerequisites

## Before running the scripts, ensure you have the following installed:

	•	Java JDK 17 or later: Required to run Java applications.
	•	JBang: Simplifies running Java scripts. 

## Scripts in This Repository

Below is a brief overview of each script and its purpose:

	1.	Day 1 
     - Historian Hysteria: Analyzes historical location data to determine possible locations of the missing Chief Historian.
     - Historian Hysteria pat 2: Evaluates how often each number from the left list appears in the right list and computes a total similarity score. This considers potential misinterpretations or errors in recording the location IDs.
	2.	Day 2
    - Reactor Safety Check: Assesses safety reports from the Red-Nosed Reindeer nuclear plant for compliance with safety standards.
    - Reactor Safety Check With Dampener: Extends the Day 2 safety check to include a tolerance for one faulty level per report, simulating a problem dampener device.

## Running the Scripts

To run any script in this repository, use the following general command:

jbang <script-name>.java --file path/to/your/input.txt

Replace <script-name> with the actual script file name and path/to/your/input.txt with the path to your input data file.

Adding New Solutions

To add a new solution:

	1.	Create a new Java file using the naming convention DayX_<PuzzleName>.java.
	2.	Implement the solution using Java and, optionally, Picocli for argument parsing.
	3.	Update this README with the new script’s details under “Scripts in This Repository.”

Contributing

Contributions to this repository are welcome. Please ensure your pull requests are well-documented and tested. For major changes, please open an issue first to discuss what you would like to change.

License

This project is licensed under the MIT License - see the LICENSE file for details.

This README template provides a comprehensive introduction and guide to your Advent of Code 2024 repository, ensuring that contributors and users can easily understand and interact with your solutions. Adjust the specifics as necessary to fit the actual content and structure of your repository.
