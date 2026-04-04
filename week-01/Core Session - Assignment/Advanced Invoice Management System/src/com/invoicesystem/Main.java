package com.invoicesystem;

import com.invoicesystem.enums.CustomerType;
import com.invoicesystem.enums.PaymentMethod;
import com.invoicesystem.enums.ProductCategory;
import com.invoicesystem.enums.TaxType;
import com.invoicesystem.model.*;
import com.invoicesystem.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final CustomerService customerService = new CustomerService();
    private static final ProductService productService = new ProductService();
    private static final InvoiceService invoiceService =
            new InvoiceService(productService, customerService);
    private static final AnalyticsService analyticsService =
            new AnalyticsService(invoiceService, customerService, productService);
    private static final FileService fileService = new FileService();


    public static void main(String[] args){
        while (true) {
            System.out.println("\n=== INVOICE MANAGEMENT SYSTEM ===");
            System.out.println("1. Customer Management");
            System.out.println("2. Product Management");
            System.out.println("3. Invoice Operations");
            System.out.println("4. Reports & Analytics");
            System.out.println("5. File Operations");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> customerMenu();
                case 2 -> productMenu();
                case 3 -> invoiceMenu();
                case 4 -> analyticsMenu();
                case 5 -> fileMenu();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
    private static void fileMenu() {
        System.out.println("\n5.1 Save Data");
        System.out.println("5.2 Load Data");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
                fileService.saveToFile(customerService.getAllCustomers(), "data/customers.dat");
                fileService.saveToFile(productService.getAllProducts(), "data/products.dat");
                fileService.saveToFile(invoiceService.getAllInvoices(), "data/invoices.dat");
                System.out.println("Saved!");
            }

            case 2 -> {
                List<Customer> customers = fileService.loadFromFile("customers.dat");
                if (customers != null) customerService.setCustomers(customers);

                List<Product> products = fileService.loadFromFile("products.dat");
                if (products != null) productService.setProducts(products);

                List<Invoice> invoices = fileService.loadFromFile("invoices.dat");
                if (invoices != null) invoiceService.setInvoices(invoices);

                System.out.println("Loaded!");
            }
        }
    }
    private static void invoiceMenu() {
        System.out.println("\n3.1 Create Invoice");
        System.out.println("3.2 View All Invoices");
        System.out.println("3.3 Process Payment");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> createInvoice();
            case 2 -> invoiceService.getAllInvoices()
                    .forEach(System.out::println);
            case 3 -> processPayment();
        }
    }

    private static void createInvoice() {
        System.out.println("Enter Customer ID:");
        String customerId = scanner.nextLine();

        Invoice invoice = invoiceService.createInvoice(customerId);

        if (invoice == null) {
            System.out.println("Customer not found");
            return;
        }

        while (true) {
            System.out.println("Enter Product ID (or 'done'):");
            String pid = scanner.nextLine();

            if (pid.equalsIgnoreCase("done")) break;

            System.out.println("Quantity:");
            int qty = scanner.nextInt();
            scanner.nextLine();

            invoiceService.addProductToInvoice(
                    invoice.getInvoiceNumber(),
                    pid,
                    qty,
                    CustomerType.REGULAR
            );
        }

        System.out.println("Invoice Created: " + invoice.getInvoiceNumber());
    }

    private static void processPayment() {
        System.out.println("Invoice Number:");
        String inv = scanner.nextLine();

        System.out.println("Amount:");
        double amt = scanner.nextDouble();

        invoiceService.processPayment(inv, amt, PaymentMethod.CASH);
        System.out.println("Payment processed!");
    }

    // ================= ANALYTICS MENU =================

    private static void analyticsMenu() {
        System.out.println("\n4.1 Top Customers");
        System.out.println("4.2 Revenue by Category");
        System.out.println("4.3 Best Products");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> analyticsService.getTop5Customers()
                    .forEach(System.out::println);

            case 2 -> analyticsService.getRevenueByCategory()
                    .forEach((k, v) -> System.out.println(k + ": " + v));

            case 3 -> analyticsService.getBestSellingProducts()
                    .forEach(System.out::println);
        }
    }


    private static void productMenu() {
        System.out.println("\n2.1 Add Product");
        System.out.println("2.2 View Products");
        System.out.println("2.3 Low Stock");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addProduct();
            case 2 -> productService.getAllProducts()
                    .forEach(System.out::println);
            case 3 -> productService.getLowStockProducts()
                    .forEach(System.out::println);
        }
    }

    private static void addProduct() {
        System.out.println("Enter ID:");
        String id = scanner.nextLine();

        System.out.println("Enter Name:");
        String name = scanner.nextLine();

        System.out.println("Enter Price:");
        double price = scanner.nextDouble();

        Product product = new PhysicalProduct(
                id, name, 100, 10,
                "Default Supplier", price,
                ProductCategory.ELECTRONICS,
                TaxType.GST, 0,
                1.0, 10.0, false
        );

        productService.addProduct(product);
        System.out.println("Product added!");
    }

    private static void customerMenu() {
        System.out.println("\n1.1 Add Customer");
        System.out.println("1.2 Search Customer");
        System.out.println("1.3 View All Customers");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addCustomer();
            case 2 -> searchCustomer();
            case 3 -> customerService.getAllCustomers()
                    .forEach(System.out::println);
        }
    }

    private static void addCustomer() {
        System.out.println("Enter ID:");
        String id = scanner.nextLine();

        System.out.println("Enter Name:");
        String name = scanner.nextLine();

        System.out.println("Enter Email:");
        String email = scanner.nextLine();

        System.out.println("Enter Phone:");
        String phone = scanner.nextLine();

        System.out.println("Enter Address:");
        String address = scanner.nextLine();

        System.out.println("Type: 1-Regular 2-Premium 3-Corporate");
        int type = scanner.nextInt();
        scanner.nextLine();

        Customer customer;

        if (type == 2) {
            customer = new PremiumCustomer(id, name, email, phone, address,
                    LocalDate.now(), 10, 100);
        } else if (type == 3) {
            customer = new CorporateCustomer(id, name, email, phone, address,
                    LocalDate.now(), 10000, 30, false);
        } else {
            customer = new RegularCustomer(id, name, email, phone, address,
                    LocalDate.now());
        }

        customerService.addCustomer(customer);
        System.out.println("Customer added!");
    }

    private static void searchCustomer() {
        System.out.println("Enter name:");
        String name = scanner.nextLine();

        customerService.findCustomersByName(name)
                .forEach(System.out::println);
    }


}