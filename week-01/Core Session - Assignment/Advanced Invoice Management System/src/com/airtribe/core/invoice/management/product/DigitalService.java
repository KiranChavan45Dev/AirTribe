package com.airtribe.core.invoice.management.product;

import com.airtribe.core.invoice.management.customer.Customer;

import java.io.Serial;
import java.io.Serializable;

public class DigitalService extends Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String link;

    public DigitalService(String id, String name, ProductCategory category, Double basePrice, Double taxRate, Integer stockQuantity, Integer reorderLevel, String supplierInfo, String link) {
        super(id, name, category, basePrice, taxRate, stockQuantity, reorderLevel, supplierInfo);
        this.link = link;
    }

    public DigitalService(String id, String name, ProductCategory category, Double basePrice, Double taxRate, Integer stockQuantity, Integer reorderLevel, String supplierInfo, Double seasonalDiscount, String link) {
        super(id, name, category, basePrice, taxRate, stockQuantity, reorderLevel, supplierInfo, seasonalDiscount);
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public Double getFinalPrice(Customer customer) {
        return super.getFinalPrice(customer);
    }

    @Override
    public String toString() {
        return "DigitalService{" +
                "link='" + link + '\'' +
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
