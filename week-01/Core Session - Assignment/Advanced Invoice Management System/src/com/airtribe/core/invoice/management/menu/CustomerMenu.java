package com.airtribe.core.invoice.management.menu;

import com.airtribe.core.invoice.management.customer.*;
import com.airtribe.core.invoice.management.manager.CustomerManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerMenu {

    public static void customerMenu(Scanner scanner, CustomerManager customerManager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Add Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Search Customer");
            System.out.println("4. View All Customers");
            System.out.println("5. View Purchase History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addCustomerCLI(scanner, customerManager);
                case 2 -> updateCustomerCLI(scanner, customerManager);
                case 3 -> searchCustomerCLI(scanner, customerManager);
                case 4 -> viewAllCustomersCLI(customerManager);
                case 5 -> viewPurchaseHistoryCLI(scanner, customerManager);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // --- Add Customer ---
    private static void addCustomerCLI(Scanner scanner, CustomerManager customerManager) {
        System.out.println("Select Customer Type:");
        System.out.println("1. Regular");
        System.out.println("2. Premium");
        System.out.println("3. Corporate");
        int type = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter ID (numeric): ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        Customer customer = null;

        switch (type) {
            case 1 -> customer = new RegularCustomer(id, name, email, phone, address, new ArrayList<>(), LocalDate.now());
            case 2 -> {
                System.out.print("Enter Discount Percentage: ");
                double discount = scanner.nextDouble();
                System.out.print("Enter Loyalty Points: ");
                int points = scanner.nextInt();
                scanner.nextLine();
                customer = new PremiumCustomer(id, name, email, phone, address, new ArrayList<>(), LocalDate.now(), discount, points);
            }
            case 3 -> {
                System.out.print("Enter Credit Limit: ");
                double limit = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Enter Payment Terms: ");
                String terms = scanner.nextLine();
                System.out.print("Is Tax Exempt (true/false): ");
                boolean taxExempt = scanner.nextBoolean();
                scanner.nextLine();
                customer = new CorporateCustomer(id, name, email, phone, address, new ArrayList<>(), LocalDate.now(), limit, terms, taxExempt);
            }
            default -> System.out.println("Invalid type selected.");
        }

        if (customer != null) {
            try {
                customerManager.addCustomer(customer);
                System.out.println("✅ Customer added successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- Update Customer ---
    private static void updateCustomerCLI(Scanner scanner, CustomerManager customerManager) {
        System.out.print("Enter Customer ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter new Email (or leave blank): ");
        String email = scanner.nextLine();
        if (email.isBlank()) email = null;

        System.out.print("Enter new Phone (or leave blank): ");
        String phone = scanner.nextLine();
        if (phone.isBlank()) phone = null;

        System.out.print("Enter new Address (or leave blank): ");
        String address = scanner.nextLine();
        if (address.isBlank()) address = null;

        customerManager.updateCustomer(id, email, phone, address);
    }

    // --- Search Customer ---
    private static void searchCustomerCLI(Scanner scanner, CustomerManager customerManager) {
        System.out.print("Enter name/email/ID to search: ");
        String keyword = scanner.nextLine();
        customerManager.searchCustomer(keyword);
    }

    // --- View All Customers ---
    private static void viewAllCustomersCLI(CustomerManager customerManager) {
        System.out.println("\n=== CUSTOMER LIST ===");
        if (customerManager.getAllCustomers().isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customerManager.getAllCustomers().forEach(System.out::println);
        }
    }

    // --- View Purchase History ---
    private static void viewPurchaseHistoryCLI(Scanner scanner, CustomerManager customerManager) {
        System.out.print("Enter Customer ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        customerManager.viewCustomerHistory(id);
    }
}
