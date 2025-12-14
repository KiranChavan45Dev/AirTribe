package com.airtribe.core.invoice.management.menu;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.*;
import com.airtribe.core.invoice.management.manager.*;
import com.airtribe.core.invoice.management.product.Product;
import com.airtribe.core.invoice.management.strategy.discount.*;

import java.util.List;
import java.util.Scanner;

public class InvoiceMenu {
    // --- INVOICE OPERATIONS MENU ---
    public static void invoiceMenu(Scanner scanner, InvoiceManager invoiceManager,
                                    CustomerManager customerManager, ProductManager productManager) {

        boolean back = false;
        while (!back) {
            System.out.println("\n--- INVOICE OPERATIONS ---");
            System.out.println("1. Create New Invoice");
            System.out.println("2. Add Item to Invoice");
            System.out.println("3. Apply Discount");
            System.out.println("4. Record Payment");
            System.out.println("5. View All Invoices");
            System.out.println("6. Search Invoice by Number");
            System.out.println("7. View Unpaid Invoices");
            System.out.println("8. Delete Invoice");
            System.out.println("9. Print Invoice Summary");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createInvoiceCLI(scanner, invoiceManager, customerManager);
                case 2 -> addItemToInvoiceCLI(scanner, invoiceManager, productManager);
                case 3 -> applyDiscountCLI(scanner, invoiceManager);
                case 4 -> recordPaymentCLI(scanner, invoiceManager);
                case 5 -> invoiceManager.getAllInvoices().forEach(System.out::println);
                case 6 -> searchInvoiceCLI(scanner, invoiceManager);
                case 7 -> invoiceManager.getUnpaidInvoices().forEach(System.out::println);
                case 8 -> deleteInvoiceCLI(scanner, invoiceManager);
                case 9 -> invoiceManager.printInvoiceSummary();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private static void createInvoiceCLI(Scanner scanner, InvoiceManager invoiceManager, CustomerManager customerManager) {
        System.out.print("Enter Customer ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Customer customer = customerManager.getAllCustomers().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        Invoice invoice = invoiceManager.createInvoice(customer);
        System.out.println("Invoice created successfully: " + invoice.getInvoiceNumber());
    }

    private static void addItemToInvoiceCLI(Scanner scanner, InvoiceManager invoiceManager, ProductManager productManager) {
        System.out.print("Enter Invoice Number: ");
        String invoiceNum = scanner.nextLine();

        Invoice invoice = invoiceManager.searchByInvoiceNumber(invoiceNum);
        if (invoice == null) {
            System.out.println("Invoice not found!");
            return;
        }

        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();
        var product = productManager.searchById(productId);

        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter Quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        InvoiceItem item = new InvoiceItem(product, qty);
        invoiceManager.addItemToInvoice(invoice, item);
        System.out.println("Item added successfully.");
    }

    private static void applyDiscountCLI(Scanner scanner, InvoiceManager invoiceManager) {
        System.out.print("Enter Invoice Number: ");
        String invoiceNum = scanner.nextLine();

        Invoice invoice = invoiceManager.searchByInvoiceNumber(invoiceNum);
        if (invoice == null) {
            System.out.println("Invoice not found!");
            return;
        }

        System.out.println("Select Discount Type:");
        System.out.println("1. Fixed Amount");
        System.out.println("2. Percentage");
        System.out.println("3. Bulk Discount");
        int type = scanner.nextInt();
        scanner.nextLine();

        switch (type) {
            case 1 -> {
                System.out.print("Enter discount amount: ");
                double amt = scanner.nextDouble();
                scanner.nextLine();
                invoiceManager.applyDiscount(invoice, new com.airtribe.core.invoice.management.strategy.discount.FixedDiscount(amt));
            }
            case 2 -> {
                System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
                double rate = scanner.nextDouble();
                scanner.nextLine();
                invoiceManager.applyDiscount(invoice, new com.airtribe.core.invoice.management.strategy.discount.PercentageDiscount(rate));
            }
            case 3 -> {
                System.out.print("Enter quantity threshold: ");
                int threshold = scanner.nextInt();
                System.out.print("Enter discount rate (e.g., 0.2 for 20%): ");
                double rate = scanner.nextDouble();
                scanner.nextLine();
                invoiceManager.applyDiscount(invoice, new com.airtribe.core.invoice.management.strategy.discount.BulkDiscount(threshold, rate));
            }
            default -> System.out.println("Invalid type.");
        }
        System.out.println("Discount applied successfully.");
    }

    private static void recordPaymentCLI(Scanner scanner, InvoiceManager invoiceManager) {
        System.out.print("Enter Invoice Number: ");
        String invoiceNum = scanner.nextLine();
        Invoice invoice = invoiceManager.searchByInvoiceNumber(invoiceNum);

        if (invoice == null) {
            System.out.println("Invoice not found!");
            return;
        }

        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Select Payment Method: ");
        System.out.println("1. CASH");
        System.out.println("2. CARD");
        System.out.println("3. UPI");
        System.out.println("4. NET BANKING");
        int method = scanner.nextInt();
        scanner.nextLine();

        PaymentMethod paymentMethod = switch (method) {
            case 1 -> PaymentMethod.CASH;
            case 2 -> PaymentMethod.CARD;
            case 3 -> PaymentMethod.UPI;
            case 4 -> PaymentMethod.NET_BANKING;
            default -> PaymentMethod.CASH;
        };

        invoiceManager.recordPayment(invoice, amount, paymentMethod);
    }

    private static void searchInvoiceCLI(Scanner scanner, InvoiceManager invoiceManager) {
        System.out.print("Enter Invoice Number: ");
        String invoiceNum = scanner.nextLine();

        Invoice invoice = invoiceManager.searchByInvoiceNumber(invoiceNum);
        if (invoice != null) {
            System.out.println(invoice);
        } else {
            System.out.println("No invoice found with that number.");
        }
    }

    private static void deleteInvoiceCLI(Scanner scanner, InvoiceManager invoiceManager) {
        System.out.print("Enter Invoice Number to delete: ");
        String invoiceNum = scanner.nextLine();

        boolean removed = invoiceManager.deleteInvoice(invoiceNum);
        if (removed) {
            System.out.println("Invoice deleted successfully.");
        } else {
            System.out.println("Invoice not found.");
        }
    }

}

