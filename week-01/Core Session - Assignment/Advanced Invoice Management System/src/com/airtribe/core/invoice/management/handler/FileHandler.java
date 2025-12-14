package com.airtribe.core.invoice.management.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {

    // Write list of strings to TXT
    public static void writeToTxt(String filePath, List<String> lines) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data written to TXT successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read lines from TXT
    public static List<String> readFromTxt(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Write CSV
    public static void writeToCsv(String filePath, List<String[]> rows) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (String[] row : rows) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            System.out.println("Data written to CSV successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read CSV
    public static List<String[]> readFromCsv(String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}