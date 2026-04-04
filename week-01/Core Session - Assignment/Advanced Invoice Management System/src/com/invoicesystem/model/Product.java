package com.invoicesystem.model;

import com.invoicesystem.enums.ProductCategory;
import com.invoicesystem.enums.TaxType;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Base Product class with common attributes and methods
 */
public abstract class Product implements Serializable {
    protected String id;
    protected String name;
    protected int stockQuantity;
    protected int reorderLevel;
    protected String supplierInfo;
    protected double basePrice;
    protected ProductCategory category;
    protected TaxType taxType;
    protected double seasonalDiscount; // percentage
    protected LocalDate lastRestockDate;

    public Product(String id, String name, int stockQuantity, int reorderLevel,
                   String supplierInfo, double basePrice, ProductCategory category,
                   TaxType taxType, double seasonalDiscount) {
        this.id = id;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.supplierInfo = supplierInfo;
        this.basePrice = basePrice;
        this.category = category;
        this.taxType = taxType;
        this.seasonalDiscount = seasonalDiscount;
        this.lastRestockDate = LocalDate.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public String getSupplierInfo() { return supplierInfo; }
    public void setSupplierInfo(String supplierInfo) { this.supplierInfo = supplierInfo; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public ProductCategory getCategory() { return category; }
    public void setCategory(ProductCategory category) { this.category = category; }

    public TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }

    public double getSeasonalDiscount() { return seasonalDiscount; }
    public void setSeasonalDiscount(double seasonalDiscount) { this.seasonalDiscount = seasonalDiscount; }

    public LocalDate getLastRestockDate() { return lastRestockDate; }
    public void setLastRestockDate(LocalDate lastRestockDate) { this.lastRestockDate = lastRestockDate; }

    /**
     * Calculate final price after applying seasonal discount
     * @return final price
     */
    public double getFinalPrice() {
        return basePrice * (1 - seasonalDiscount / 100);
    }

    /**
     * Check if product needs restocking
     * @return true if stock is at or below reorder level
     */
    public boolean needsRestock() {
        return stockQuantity <= reorderLevel;
    }

    /**
     * Restock product with given quantity
     * @param quantity quantity to add to stock
     */
    public void restock(int quantity) {
        if (quantity > 0) {
            stockQuantity += quantity;
            lastRestockDate = LocalDate.now();
        }
    }

    /**
     * Sell product with given quantity
     * @param quantity quantity to sell
     * @return true if successful, false if insufficient stock
     */
    public boolean sell(int quantity) {
        if (quantity > 0 && stockQuantity >= quantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Abstract method to get product type
     * @return product type description
     */
    public abstract String getProductType();

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", reorderLevel=" + reorderLevel +
                ", supplierInfo='" + supplierInfo + '\'' +
                ", basePrice=" + basePrice +
                ", category=" + category +
                ", taxType=" + taxType +
                ", seasonalDiscount=" + seasonalDiscount +
                ", lastRestockDate=" + lastRestockDate +
                '}';
    }
}