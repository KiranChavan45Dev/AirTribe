package com.airtribe.core.invoice.management.menu;

import com.airtribe.core.invoice.management.manager.CustomerManager;
import com.airtribe.core.invoice.management.manager.InvoiceManager;
import com.airtribe.core.invoice.management.manager.ProductManager;

import java.util.Scanner;

public class ReportMenu {
    // --- REPORTS & ANALYTICS MENU ---
    public static void reportsMenu(Scanner scanner,
                                   CustomerManager customerManager,
                                   ProductManager productManager,
                                   InvoiceManager invoiceManager) {

        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTS & ANALYTICS ---");
            System.out.println("1. View Top 5 Customers by Total Spend");
            System.out.println("2. Average Order Value by Customer Type");
            System.out.println("3. Sales by Product Category and Month");
            System.out.println("4. Low Stock Product Alerts");
            System.out.println("5. Filter Invoices by Date and Payment Status");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> topCustomersReport(customerManager);
                case 2 -> averageOrderValueReport(customerManager);
                case 3 -> salesByCategoryReport(invoiceManager);
                case 4 -> lowStockAlertReport(productManager);
                case 5 -> filterInvoicesReport(scanner, invoiceManager);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private static void topCustomersReport(CustomerManager customerManager) {
        var topCustomers = com.airtribe.core.invoice.management.analytics.AnalyticsService
                .topCustomers(customerManager.getAllCustomers());

        System.out.println("\n=== TOP 5 CUSTOMERS BY TOTAL PURCHASE VALUE ===");
        if (topCustomers.isEmpty()) {
            System.out.println("No customer data available.");
            return;
        }

        int rank = 1;
        for (var customer : topCustomers) {
            System.out.printf("%d. %s | Total Spent: ₹%.2f%n",
                    rank++,
                    customer.getName(),
                    customer.getPurchaseHistory() == null ? 0.0 :
                            customer.getPurchaseHistory().stream().mapToDouble(inv -> inv.getTotalAmount()).sum());
        }
    }

    private static void averageOrderValueReport(CustomerManager customerManager) {
        var avgMap = com.airtribe.core.invoice.management.analytics.AnalyticsService
                .averageOrderValueByType(customerManager.getAllCustomers());

        System.out.println("\n=== AVERAGE ORDER VALUE BY CUSTOMER TYPE ===");
        if (avgMap.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        avgMap.forEach((type, avg) ->
                System.out.printf("%-12s : ₹%.2f%n", type, avg)
        );
    }

    private static void salesByCategoryReport(InvoiceManager invoiceManager) {
        var salesData = com.airtribe.core.invoice.management.analytics.AnalyticsService
                .salesByCategoryAndMonth(invoiceManager.getAllInvoices());

        System.out.println("\n=== SALES BY PRODUCT CATEGORY AND MONTH ===");
        if (salesData.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        salesData.forEach((category, monthMap) -> {
            System.out.println("\nCategory: " + category);
            monthMap.forEach((month, total) ->
                    System.out.printf("  Month %d : ₹%.2f%n", month, total));
        });
    }

    private static void lowStockAlertReport(ProductManager productManager) {
        var lowStock = com.airtribe.core.invoice.management.analytics.AnalyticsService
                .lowStockProducts(productManager.getAllProducts());

        System.out.println("\n=== LOW STOCK ALERTS ===");
        if (lowStock.isEmpty()) {
            System.out.println("All products have sufficient stock.");
            return;
        }

        lowStock.forEach(p ->
                System.out.printf("Product: %-20s | Stock: %-5d | Reorder Level: %-5d%n",
                        p.getName(), p.getStockQuantity(), p.getReorderLevel()));
    }

    private static void filterInvoicesReport(Scanner scanner, InvoiceManager invoiceManager) {
        System.out.println("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.println("Enter End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        System.out.println("Enter Payment Status (PAID, PENDING, PARTIAL): ");
        String status = scanner.nextLine();

        var filtered = com.airtribe.core.invoice.management.analytics.AnalyticsService
                .filterInvoices(invoiceManager.getAllInvoices(),
                        java.time.LocalDate.parse(startDate),
                        java.time.LocalDate.parse(endDate),
                        status);

        System.out.println("\n=== FILTERED INVOICES ===");
        if (filtered.isEmpty()) {
            System.out.println("No invoices match the criteria.");
            return;
        }

        filtered.forEach(System.out::println);
    }

}
