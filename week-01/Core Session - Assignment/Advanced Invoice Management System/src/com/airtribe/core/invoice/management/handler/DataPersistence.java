package com.airtribe.core.invoice.management.handler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {

    /**
     * Save any Serializable list to the given file.
     */
    public static <T extends Serializable> void saveData(String filePath, List<T> dataList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(dataList);
            System.out.println("Data saved successfully: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving data to " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Load any Serializable list from file safely.
     * If file does not exist or is empty, return an empty list instead of crashing.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> loadData(String filePath) {
        File file = new File(filePath);

        // Handle missing file gracefully
        if (!file.exists()) {
            System.out.println("No data file found for: " + filePath + " (skipping load)");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<T>) obj;
            } else {
                System.out.println("Invalid data format in: " + filePath);
                return new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
