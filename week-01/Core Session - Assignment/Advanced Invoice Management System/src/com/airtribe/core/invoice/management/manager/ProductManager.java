package com.airtribe.core.invoice.management.manager;

import com.airtribe.core.invoice.management.product.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles creation, update, search, and stock management for products.
 */
public class ProductManager {

    private final List<Product> products = new ArrayList<>();

    // --- Add Product ---
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getName());
    }

    // --- Get All Products ---
    public List<Product> getAllProducts() {
        return products;
    }

    // --- Search by ID ---
    public Product searchById(String id) {
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    // --- Search by Category ---
    public List<Product> searchByCategory(ProductCategory category) {
        return products.stream()
                .filter(p -> p.getCategory() == category)
                .collect(Collectors.toList());
    }

    // --- Search by Name ---
    public List<Product> searchByName(String keyword) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // --- Update Stock ---
    public void updateStock(String productId, int newStock) {
        Product p = searchById(productId);
        if (p != null) {
            p.setStockQuantity(newStock);
            System.out.println("Stock updated for " + p.getName() + ": " + newStock + " units");
        } else {
            System.out.println("Product not found!");
        }
    }

    // --- Apply Seasonal Discount ---
    public void applySeasonalDiscount(double discountPercent) {
        products.forEach(p -> p.setSeasonalDiscount(discountPercent));
        System.out.println("Applied " + discountPercent + "% seasonal discount to all products.");
    }

    // --- Low Stock Alert ---
    public List<Product> getLowStockProducts() {
        return products.stream()
                .filter(p -> p.getStockQuantity() <= p.getReorderLevel())
                .collect(Collectors.toList());
    }

    // --- Remove Product ---
    public boolean removeProduct(String productId) {
        boolean removed = products.removeIf(p -> p.getId().equalsIgnoreCase(productId));
        if (removed) {
            System.out.println("Product removed with ID: " + productId);
        } else {
            System.out.println("No product found with ID: " + productId);
        }
        return removed;
    }

    // --- Print Product Catalog ---
    public void printProductCatalog() {
        System.out.println("\n=== PRODUCT CATALOG ===");
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        products.forEach(p -> {
            System.out.printf("ID: %-6s | Name: %-20s | Category: %-12s | Price: ₹%-8.2f | Stock: %-5d | Discount: %-5.1f%%%n",
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getBasePrice(),
                    p.getStockQuantity(),
                    p.getSeasonalDiscount());
        });
    }
}
