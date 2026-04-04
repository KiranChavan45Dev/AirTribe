package com.invoicesystem.model;

import com.invoicesystem.enums.ProductCategory;
import com.invoicesystem.enums.TaxType;

import java.io.Serializable;

/**
 * Physical product that requires shipping
 */
public class PhysicalProduct extends Product implements Serializable {
    private double weight; // in kg
    private double dimensions; // in cm^3 (simplified as single dimension for now)
    private boolean requiresRefrigeration;

    public PhysicalProduct(String id, String name, int stockQuantity, int reorderLevel,
                           String supplierInfo, double basePrice, ProductCategory category,
                           TaxType taxType, double seasonalDiscount, double weight,
                           double dimensions, boolean requiresRefrigeration) {
        super(id, name, stockQuantity, reorderLevel, supplierInfo, basePrice, category,
                taxType, seasonalDiscount);
        this.weight = weight;
        this.dimensions = dimensions;
        this.requiresRefrigeration = requiresRefrigeration;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDimensions() {
        return dimensions;
    }

    public void setDimensions(double dimensions) {
        this.dimensions = dimensions;
    }

    public boolean isRequiresRefrigeration() {
        return requiresRefrigeration;
    }

    public void setRequiresRefrigeration(boolean requiresRefrigeration) {
        this.requiresRefrigeration = requiresRefrigeration;
    }

    @Override
    public String getProductType() {
        return "Physical";
    }

    @Override
    public String toString() {
        return "PhysicalProduct{" +
                "weight=" + weight +
                ", dimensions=" + dimensions +
                ", requiresRefrigeration=" + requiresRefrigeration +
                ", id='" + id + '\'' +
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