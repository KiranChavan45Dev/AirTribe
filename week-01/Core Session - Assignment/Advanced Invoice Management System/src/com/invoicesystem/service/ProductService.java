package com.invoicesystem.service;

import com.invoicesystem.model.DigitalProduct;
import com.invoicesystem.model.PhysicalProduct;
import com.invoicesystem.model.Product;
import com.invoicesystem.enums.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing product operations
 */
public class ProductService {
    private List<Product> products;

    public ProductService() {
        this.products = new ArrayList<>();
    }

    /**
     * Add a new product
     * @param product product to add
     * @return true if successful
     */
    public boolean addProduct(Product product) {
        if (product == null || findProductById(product.getId()).isPresent()) {
            return false;
        }
        products.add(product);
        return true;
    }

    /**
     * Find product by ID
     * @param id product ID
     * @return optional containing product if found
     */
    public Optional<Product> findProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Find products by name (partial match)
     * @param name name to search for
     * @return list of matching products
     */
    public List<Product> findProductsByName(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>();
        }
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    /**
     * Find products by category
     * @param category product category
     * @return list of products in specified category
     */
    public List<Product> findProductsByCategory(ProductCategory category) {
        return products.stream()
                .filter(p -> p.getCategory().equals(category))
                .toList();
    }

    /**
     * Update product information
     * @param id product ID
     * @param updatedProduct updated product data
     * @return true if successful
     */
    public boolean updateProduct(String id, Product updatedProduct) {
        Optional<Product> existingProductOpt = findProductById(id);
        if (existingProductOpt.isEmpty() || updatedProduct == null) {
            return false;
        }

        Product existingProduct = existingProductOpt.get();
        // Update fields while preserving ID
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setReorderLevel(updatedProduct.getReorderLevel());
        existingProduct.setSupplierInfo(updatedProduct.getSupplierInfo());
        existingProduct.setBasePrice(updatedProduct.getBasePrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setTaxType(updatedProduct.getTaxType());
        existingProduct.setSeasonalDiscount(updatedProduct.getSeasonalDiscount());
        existingProduct.setLastRestockDate(updatedProduct.getLastRestockDate());

        // For physical and digital products, update specific fields
        if (updatedProduct instanceof PhysicalProduct && existingProduct instanceof PhysicalProduct) {
            PhysicalProduct updatedPhys = (PhysicalProduct) updatedProduct;
            PhysicalProduct existingPhys = (PhysicalProduct) existingProduct;
            existingPhys.setWeight(updatedPhys.getWeight());
            existingPhys.setDimensions(updatedPhys.getDimensions());
            existingPhys.setRequiresRefrigeration(updatedPhys.isRequiresRefrigeration());
        } else if (updatedProduct instanceof DigitalProduct && existingProduct instanceof DigitalProduct) {
            DigitalProduct updatedDig = (DigitalProduct) updatedProduct;
            DigitalProduct existingDig = (DigitalProduct) existingProduct;
            existingDig.setDownloadLink(updatedDig.getDownloadLink());
            existingDig.setLicenseKey(updatedDig.getLicenseKey());
            existingDig.setSubscription(updatedDig.isSubscription());
        }

        return true;
    }

    /**
     * Get all products
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Get products that need restocking
     * @return list of products needing restock
     */
    public List<Product> getLowStockProducts() {
        return products.stream()
                .filter(Product::needsRestock)
                .toList();
    }

    /**
     * Restock a product
     * @param productId product ID
     * @param quantity quantity to add
     * @return true if successful
     */
    public boolean restockProduct(String productId, int quantity) {
        return findProductById(productId)
                .map(product -> {
                    product.restock(quantity);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Sell a product
     * @param productId product ID
     * @param quantity quantity to sell
     * @return true if successful
     */
    public boolean sellProduct(String productId, int quantity) {
        return findProductById(productId)
                .map(product -> {
                    boolean success = product.sell(quantity);
                    return success;
                })
                .orElse(false);
    }

    /**
     * Calculate sales velocity for a product (simplified)
     * @param productId product ID
     * @return sales velocity (units sold per day - placeholder)
     */
    public double calculateSalesVelocity(String productId) {
        // In a real system, this would calculate based on actual sales history
        // For now, returning a placeholder value
        return 0.0;
    }

    /**
     * Get customer-type specific pricing for a product
     * @param productId product ID
     * @param customerType customer type
     * @return price for customer type
     */
    public double getPriceForCustomerType(String productId, com.invoicesystem.enums.CustomerType customerType) {
        return findProductById(productId)
                .map(product -> {
                    double basePrice = product.getFinalPrice();
                    // Apply customer-type specific discounts
                    switch (customerType) {
                        case REGULAR:
                            return basePrice;
                        case PREMIUM:
                            // Premium customers get additional 5% discount
                            return basePrice * 0.95;
                        case CORPORATE:
                            // Corporate customers get additional 10% discount
                            return basePrice * 0.90;
                        default:
                            return basePrice;
                    }
                })
                .orElse(0.0);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}