package com.airtribe.core.invoice.management.menu;

import com.airtribe.core.invoice.management.manager.ProductManager;
import com.airtribe.core.invoice.management.product.DigitalService;
import com.airtribe.core.invoice.management.product.PhysicalProduct;
import com.airtribe.core.invoice.management.product.Product;
import com.airtribe.core.invoice.management.product.ProductCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProductMenu {

    public static void productMenu(Scanner scanner, ProductManager productManager) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- PRODUCT MANAGEMENT ---");
            System.out.println("1. Add Product");
            System.out.println("2. Update Stock");
            System.out.println("3. Search Product");
            System.out.println("4. View All Products");
            System.out.println("5. View Low Stock Products");
            System.out.println("6. Apply Seasonal Discount");
            System.out.println("7. Remove Product");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addProductCLI(scanner, productManager);
                case 2 -> updateStockCLI(scanner, productManager);
                case 3 -> searchProductCLI(scanner, productManager);
                case 4 -> productManager.printProductCatalog();
                case 5 -> {
                    List<Product> lowStock = productManager.getLowStockProducts();
                    if (lowStock.isEmpty()) System.out.println("✅ All products are sufficiently stocked.");
                    else {
                        System.out.println("\n⚠️ LOW STOCK PRODUCTS ⚠️");
                        lowStock.forEach(System.out::println);
                    }
                }
                case 6 -> applyDiscountCLI(scanner, productManager);
                case 7 -> removeProductCLI(scanner, productManager);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
    // --- Add Product ---
    private static void addProductCLI(Scanner scanner, ProductManager productManager) {
        System.out.println("Select Product Type:");
        System.out.println("1. Physical Product");
        System.out.println("2. Digital Service");
        int type = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();

        System.out.println("Select Category:");
        for (ProductCategory cat : ProductCategory.values()) {
            System.out.println(cat.ordinal() + 1 + ". " + cat);
        }
        int catChoice = scanner.nextInt();
        scanner.nextLine();
        ProductCategory category = ProductCategory.values()[catChoice - 1];

        System.out.print("Enter Base Price: ");
        double basePrice = scanner.nextDouble();
        System.out.print("Enter Tax Rate (%): ");
        double taxRate = scanner.nextDouble();
        System.out.print("Enter Stock Quantity: ");
        int stock = scanner.nextInt();
        System.out.print("Enter Reorder Level: ");
        int reorder = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Supplier Info: ");
        String supplier = scanner.nextLine();
        System.out.print("Enter Seasonal Discount (%): ");
        double seasonal = scanner.nextDouble();
        scanner.nextLine();

        Product product = null;
        switch (type) {
            case 1 -> {
                System.out.print("Enter Shipping Weight (kg): ");
                double weight = scanner.nextDouble();
                scanner.nextLine();
                product = new PhysicalProduct(id, name, category, basePrice, taxRate, stock, reorder, supplier, seasonal, weight);
            }
            case 2 -> {
                System.out.print("Enter Download/Access Link: ");
                String link = scanner.nextLine();
                product = new DigitalService(id, name, category, basePrice, taxRate, stock, reorder, supplier, seasonal, link);
            }
            default -> System.out.println("Invalid product type.");
        }

        if (product != null) {
            productManager.addProduct(product);
            System.out.println("Product added successfully!");
        }
    }

    // --- Update Stock ---
    private static void updateStockCLI(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter New Stock Quantity: ");
        int newStock = scanner.nextInt();
        scanner.nextLine();
        productManager.updateStock(id, newStock);
    }

    // --- Search Product ---
    private static void searchProductCLI(Scanner sc, ProductManager productManager) {
        while (true) {
            System.out.println("\n=== SEARCH PRODUCT MENU ===");
            System.out.println("1. Search by ID");
            System.out.println("2. Search by Name");
            System.out.println("3. Search by Category");
            System.out.println("4. Back to Product Management Menu");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Product ID: ");
                    String id = sc.nextLine();
                    Product p = productManager.searchById(id);
                    if (p != null) {
                        System.out.println("\nProduct Found:");
                        System.out.println(p);
                    } else {
                        System.out.println("No product found with ID: " + id);
                    }
                }

                case 2 -> {
                    System.out.print("Enter Product Name or Keyword: ");
                    String keyword = sc.nextLine();
                    List<Product> products = productManager.searchByName(keyword);
                    if (products.isEmpty()) {
                        System.out.println("No products found matching: " + keyword);
                    } else {
                        System.out.println("\nSearch Results:");
                        products.forEach(System.out::println);
                    }
                }

                case 3 -> {
                    System.out.println("Available Categories: " + Arrays.toString(ProductCategory.values()));
                    System.out.print("Enter Category: ");
                    String catInput = sc.nextLine().toUpperCase();
                    try {
                        ProductCategory category = ProductCategory.valueOf(catInput);
                        List<Product> products = productManager.searchByCategory(category);
                        if (products.isEmpty()) {
                            System.out.println("No products found in category: " + category);
                        } else {
                            System.out.println("\nProducts in " + category + ":");
                            products.forEach(System.out::println);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid category entered!");
                    }
                }

                case 4 -> {
                    System.out.println("Returning to Product Management Menu...");
                    return;
                }

                default -> System.out.println("Invalid choice! Please select between 1–4.");
            }
        }
    }
    // --- Apply Seasonal Discount ---
    private static void applyDiscountCLI(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter seasonal discount percentage to apply to all products: ");
        double discount = scanner.nextDouble();
        scanner.nextLine();
        if (discount < 0 || discount > 100) {
            System.out.println("Invalid discount value. Must be between 0 and 100.");
            return;
        }
        productManager.applySeasonalDiscount(discount);
    }

    // --- Remove Product ---
    private static void removeProductCLI(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter Product ID to remove: ");
        String id = scanner.nextLine();
        boolean removed = productManager.removeProduct(id);
        if (removed) {
            System.out.println("Product successfully removed.");
        } else {
            System.out.println("Product ID not found.");
        }
    }

}
