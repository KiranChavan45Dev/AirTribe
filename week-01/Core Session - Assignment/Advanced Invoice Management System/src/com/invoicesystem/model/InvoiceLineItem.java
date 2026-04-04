package com.invoicesystem.model;

import com.invoicesystem.enums.TaxType;

import java.io.Serializable;

public class InvoiceLineItem implements Serializable {

        private String productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double discountPercentage; // percentage discount on this line item
        private TaxType taxType;
        private double totalPrice;
        private double taxAmount;
        private double discountAmount;

    public InvoiceLineItem(String productId, String productName, int quantity,
        double unitPrice, double discountPercentage, TaxType taxType) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPercentage = discountPercentage;
        this.taxType = taxType;
        calculateAmounts();
    }

        // Getters and Setters
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
        public double getDiscountPercentage() { return discountPercentage; }
        public TaxType getTaxType() { return taxType; }
        public double getTotalPrice() { return totalPrice; }
        public double getTaxAmount() { return taxAmount; }
        public double getDiscountAmount() { return discountAmount; }

        /**
         * Calculate amounts for this line item
         */
        private void calculateAmounts() {
        // Calculate base amount
        double baseAmount = quantity * unitPrice;

        // Apply discount
        discountAmount = baseAmount * (discountPercentage / 100);
        double discountedAmount = baseAmount - discountAmount;

        // Apply tax (simplified - in reality tax rates would vary by tax type)
        double taxRate = getTaxRateForType(taxType);
        taxAmount = discountedAmount * (taxRate / 100);

        // Calculate final total
        totalPrice = discountedAmount + taxAmount;
    }

        /**
         * Get tax rate based on tax type (simplified)
         * @param taxType tax type
         * @return tax rate percentage
         */
        private double getTaxRateForType(com.invoicesystem.enums.TaxType taxType) {
        switch (taxType) {
            case GST: return 18.0; // 18% GST
            case VAT: return 20.0; // 20% VAT
            case SERVICE_TAX: return 12.0; // 12% Service Tax
            default: return 0.0;
        }
    }

}
