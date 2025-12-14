package com.airtribe.core.invoice.management.product;

import com.airtribe.core.invoice.management.customer.CorporateCustomer;
import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.customer.PremiumCustomer;

import java.io.Serial;
import java.io.Serializable;

public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected ProductCategory category;
    protected Double basePrice;
    protected Double taxRate;
    protected Integer stockQuantity;
    protected Integer reorderLevel;
    protected String supplierInfo;
    protected Double seasonalDiscount = 0.0;

    public Product(String id, String name, ProductCategory category, Double basePrice, Double taxRate, Integer stockQuantity, Integer reorderLevel, String supplierInfo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.taxRate = taxRate;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.supplierInfo = supplierInfo;
    }

    public Product(String id, String name, ProductCategory category, Double basePrice, Double taxRate, Integer stockQuantity, Integer reorderLevel, String supplierInfo, Double seasonalDiscount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.taxRate = taxRate;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.supplierInfo = supplierInfo;
        this.seasonalDiscount = seasonalDiscount;
    }

    public Double getSeasonalDiscount() {
        return seasonalDiscount;
    }

    public void setSeasonalDiscount(Double seasonalDiscount) {
        this.seasonalDiscount = seasonalDiscount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(String supplierInfo) {
        this.supplierInfo = supplierInfo;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", basePrice=" + basePrice +
                ", taxRate=" + taxRate +
                ", stockQuantity=" + stockQuantity +
                ", reorderLevel=" + reorderLevel +
                ", supplierInfo='" + supplierInfo + '\'' +
                ", seasonalDiscount=" + seasonalDiscount +
                '}';
    }

    public Double getFinalPrice(Customer customer) {
        double price = basePrice;

        // Apply seasonal discount if any
        if (seasonalDiscount != null && seasonalDiscount > 0) {
            price -= price * (seasonalDiscount / 100);
        }

        // Apply customer-type based adjustments
        if (customer instanceof PremiumCustomer premium) {
            // Premium customers get extra discount
            price -= price * (premium.getDiscountPercentage() / 100);
        } else if (customer instanceof CorporateCustomer corp && corp.isTaxExempt()) {
            // Corporate customers may be tax-exempt
            return price; // Skip tax addition
        }

        // Apply tax for non-exempt customers
        price += price * (taxRate / 100);

        return price;
    }
}
