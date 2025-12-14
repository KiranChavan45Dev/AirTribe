package com.airtribe.core.invoice.management.product;

import com.airtribe.core.invoice.management.customer.Customer;

import java.io.Serial;
import java.io.Serializable;

public class PhysicalProduct extends Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Double shippingWeight;


    public PhysicalProduct(String id, String name, ProductCategory category, Double basePrice, Double taxRate, Integer stockQuantity, Integer reorderLevel, String supplierInfo, Double shippingWeight) {
        super(id, name, category, basePrice, taxRate, stockQuantity, reorderLevel, supplierInfo);
        this.shippingWeight = shippingWeight;
    }

    public PhysicalProduct(String id, String name, ProductCategory category, Double basePrice, Double taxRate, Integer stockQuantity, Integer reorderLevel, String supplierInfo, Double seasonalDiscount, Double shippingWeight) {
        super(id, name, category, basePrice, taxRate, stockQuantity, reorderLevel, supplierInfo, seasonalDiscount);
        this.shippingWeight = shippingWeight;
    }

    public Double getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(Double shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    @Override
    public Double getFinalPrice(Customer customer) {
        // Base calculation + potential shipping surcharge
        double price = super.getFinalPrice(customer);
        // For simplicity, charge ₹10 per kg shipping
        if (shippingWeight != null && shippingWeight > 0) {
            price += shippingWeight * 10;
        }
        return price;

    }

    @Override
    public String toString() {
        return "PhysicalProduct{" +
                "shippingWeight=" + shippingWeight +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", basePrice=" + basePrice +
                ", taxRate=" + taxRate +
                ", stockQuantity=" + stockQuantity +
                ", reorderLevel=" + reorderLevel +
                ", supplierInfo='" + supplierInfo + '\'' +
                '}';
    }
}
