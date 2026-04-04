package com.invoicesystem.service;

import com.invoicesystem.model.Invoice;
import com.invoicesystem.model.InvoiceLineItem;

import java.io.*;
import java.util.List;

public class FileService {
    // Save any list to file
    public <T> boolean saveToFile(List<T> data, String filePath) {

        // 1. Validate file path
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("Error: File path is empty");
            return false;
        }

        // 2. Validate data
        if (data == null) {
            System.out.println("Error: Data is null");
            return false;
        }

        if (data.isEmpty()) {
            System.out.println("Warning: Data list is empty. Nothing to save.");
            return false;
        }

        try {
            File file = new File(filePath);

            // 3. Create directories if not present
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (!created) {
                    System.out.println("Error: Failed to create directory");
                    return false;
                }
            }

            // 4. Write file
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(file))) {

                oos.writeObject(data);

                System.out.println("Data successfully saved to: " + file.getAbsolutePath());
                return true;
            }

        } catch (IOException e) {
            System.out.println("Error while saving file: " + e.getMessage());
            return false;
        }
    }

    // Load any list from file
    @SuppressWarnings("unchecked")
    public <T> List<T> loadFromFile(String filePath) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filePath))) {

            return (List<T>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing file found: " + filePath);
            return null;
        }
    }

    public boolean backupFile(String source, String backup) {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(backup)) {

            in.transferTo(out);
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public boolean exportInvoiceToTxt(Invoice invoice, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("Invoice: " + invoice.getInvoiceNumber());
            writer.newLine();
            writer.write("Customer: " + invoice.getCustomerName());
            writer.newLine();
            writer.write("Total: " + invoice.getTotalAmount());
            writer.newLine();

            writer.write("Items:");
            writer.newLine();

            for (InvoiceLineItem item : invoice.getLineItems()) {
                writer.write(item.getProductName() + " x " + item.getQuantity());
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public boolean exportInvoicesToCSV(List<Invoice> invoices, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("InvoiceNumber,Customer,Total,Date");
            writer.newLine();

            for (Invoice inv : invoices) {
                writer.write(inv.getInvoiceNumber() + "," +
                        inv.getCustomerName() + "," +
                        inv.getTotalAmount() + "," +
                        inv.getInvoiceDate());
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
