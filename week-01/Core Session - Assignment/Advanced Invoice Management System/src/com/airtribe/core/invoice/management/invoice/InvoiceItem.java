package com.airtribe.core.invoice.management.invoice;

import com.airtribe.core.invoice.management.product.Product;

public class InvoiceItem {
    private Product product;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public InvoiceItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getBasePrice();
        calculateTotalPrice();
    }

    public InvoiceItem(Product product, int quantity, double customPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = customPrice;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        // Only apply product-level discount
        double discountedPrice = unitPrice - (unitPrice * (product.getSeasonalDiscount() / 100));
        this.totalPrice = discountedPrice * quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "product=" + product.getName() +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
