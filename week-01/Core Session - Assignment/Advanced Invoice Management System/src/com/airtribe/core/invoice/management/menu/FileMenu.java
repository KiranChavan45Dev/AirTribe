package com.airtribe.core.invoice.management.menu;

import com.airtribe.core.invoice.management.handler.BackupManager;
import com.airtribe.core.invoice.management.handler.DataPersistence;
import com.airtribe.core.invoice.management.handler.FileHandler;
import com.airtribe.core.invoice.management.manager.CustomerManager;
import com.airtribe.core.invoice.management.manager.InvoiceManager;
import com.airtribe.core.invoice.management.manager.ProductManager;
import com.airtribe.core.invoice.management.invoice.Invoice;
import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.product.Product;
import com.airtribe.core.invoice.management.wrapper.SerializableList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all file-related operations such as export, backup, and persistence.
 * Uses SerializableList wrapper for safe generic serialization.
 */
public class FileMenu {

    public static void showFileOperationsMenu(
            Scanner scanner,
            CustomerManager customerManager,
            ProductManager productManager,
            InvoiceManager invoiceManager) {

        boolean back = false;

        while (!back) {
            System.out.println("\n--- FILE OPERATIONS MENU ---");
            System.out.println("1. Export Customers to TXT");
            System.out.println("2. Export Products to CSV");
            System.out.println("3. Export Invoices to TXT");
            System.out.println("4. Backup Data File");
            System.out.println("5. Save Data (Binary)");
            System.out.println("6. Load Data (Binary)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> exportCustomersToTxt(customerManager);
                case 2 -> exportProductsToCsv(productManager);
                case 3 -> exportInvoicesToTxt(invoiceManager);
                case 4 -> backupAnyFile(scanner);
                case 5 -> saveManagersDataWrapped(customerManager, productManager, invoiceManager);
                case 6 -> loadManagersDataWrapped(customerManager, productManager, invoiceManager);
                case 0 -> back = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // --- 1. Export Customers to TXT ---
    private static void exportCustomersToTxt(CustomerManager customerManager) {
        String filePath = "exports/customers.txt";
        new File("exports").mkdirs();

        List<String> lines = new ArrayList<>();
        for (Customer c : customerManager.getAllCustomers()) {
            lines.add(String.format("%d | %s | %s | %s | %s",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()));
        }

        FileHandler.writeToTxt(filePath, lines);
    }

    // --- 2. Export Products to CSV ---
    private static void exportProductsToCsv(ProductManager productManager) {
        String filePath = "exports/products.csv";
        new File("exports").mkdirs();

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"ID", "Name", "Category", "Price", "Stock"});
        for (Product p : productManager.getAllProducts()) {
            rows.add(new String[]{
                    p.getId(),
                    p.getName(),
                    p.getCategory().toString(),
                    String.valueOf(p.getBasePrice()),
                    String.valueOf(p.getStockQuantity())
            });
        }

        FileHandler.writeToCsv(filePath, rows);
    }

    // --- 3. Export Invoices to TXT ---
    private static void exportInvoicesToTxt(InvoiceManager invoiceManager) {
        String filePath = "exports/invoices.txt";
        new File("exports").mkdirs();

        List<String> lines = new ArrayList<>();
        for (Invoice inv : invoiceManager.getAllInvoices()) {
            lines.add(String.format("%s | %s | Customer: %s | Total: ₹%.2f | Status: %s",
                    inv.getInvoiceNumber(), inv.getDate(),
                    inv.getCustomer().getName(),
                    inv.getTotalAmount(), inv.getPaymentStatus()));
        }

        FileHandler.writeToTxt(filePath, lines);
    }

    // --- 4. Backup File ---
    private static void backupAnyFile(Scanner scanner) {
        System.out.print("Enter full path of file to backup: ");
        String source = scanner.nextLine();
        System.out.print("Enter target backup directory: ");
        String target = scanner.nextLine();

        BackupManager.backupFile(source, target);
    }

    // --- 5. Save Data (Wrapped Binary Serialization) ---
    private static void saveManagersDataWrapped(CustomerManager customerManager,
                                                ProductManager productManager,
                                                InvoiceManager invoiceManager) {
        String dir = "data/";
        new File(dir).mkdirs();

        try {
            // Wrap lists in SerializableList
            SerializableList<Customer> customerList =
                    new SerializableList<>(new ArrayList<>(customerManager.getAllCustomers()));
            SerializableList<Product> productList =
                    new SerializableList<>(new ArrayList<>(productManager.getAllProducts()));
            SerializableList<Invoice> invoiceList =
                    new SerializableList<>(new ArrayList<>(invoiceManager.getAllInvoices()));

            // Save wrapped data
            DataPersistence.saveData(dir + "customers.dat", List.of(customerList));
            DataPersistence.saveData(dir + "products.dat", List.of(productList));
            DataPersistence.saveData(dir + "invoices.dat", List.of(invoiceList));

            System.out.println("All data saved successfully (wrapped)!");
        } catch (Exception e) {
            System.out.println("Error saving wrapped data: " + e.getMessage());
        }
    }

    // --- 6. Load Data (Wrapped Binary Deserialization) ---
    @SuppressWarnings("unchecked")
    private static void loadManagersDataWrapped(CustomerManager customerManager,
                                                ProductManager productManager,
                                                InvoiceManager invoiceManager) {
        String dir = "data/";

        try {
            List<SerializableList<Customer>> wrappedCustomers =
                    DataPersistence.loadData(dir + "customers.dat");
            List<SerializableList<Product>> wrappedProducts =
                    DataPersistence.loadData(dir + "products.dat");
            List<SerializableList<Invoice>> wrappedInvoices =
                    DataPersistence.loadData(dir + "invoices.dat");

            if (!wrappedCustomers.isEmpty()) {
                customerManager.getAllCustomers().clear();
                customerManager.getAllCustomers().addAll(wrappedCustomers.get(0).getData());
            }

            if (!wrappedProducts.isEmpty()) {
                productManager.getAllProducts().clear();
                productManager.getAllProducts().addAll(wrappedProducts.get(0).getData());
            }

            if (!wrappedInvoices.isEmpty()) {
                invoiceManager.getAllInvoices().clear();
                invoiceManager.getAllInvoices().addAll(wrappedInvoices.get(0).getData());
            }

            System.out.println("Wrapped data loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading wrapped data: " + e.getMessage());
        }
    }
}
