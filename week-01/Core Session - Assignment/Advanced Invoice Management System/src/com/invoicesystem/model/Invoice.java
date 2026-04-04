package com.invoicesystem.model;

import com.invoicesystem.enums.PaymentMethod;
import com.invoicesystem.enums.TaxType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Invoice class representing a sales invoice
 */
public class Invoice  implements Serializable {
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private String customerId;
    private String customerName;
    private List<InvoiceLineItem> lineItems;
    private double subtotal;
    private double taxAmount;
    private double discountAmount;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private boolean isPaid;
    private double amountPaid;
    private LocalDateTime paymentDate;

    public Invoice(String customerId, String customerName) {
        this.invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.invoiceDate = LocalDateTime.now();
        this.customerId = customerId;
        this.customerName = customerName;
        this.lineItems = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxAmount = 0.0;
        this.discountAmount = 0.0;
        this.totalAmount = 0.0;
        this.paymentMethod = null;
        this.isPaid = false;
        this.amountPaid = 0.0;
        this.paymentDate = null;
    }

    // Getters and Setters
    public String getInvoiceNumber() { return invoiceNumber; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public List<InvoiceLineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<InvoiceLineItem> lineItems) { this.lineItems = lineItems; }
    public double getSubtotal() { return subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTotalAmount() { return totalAmount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public double getAmountPaid() { return amountPaid; }
    public LocalDateTime getPaymentDate() { return paymentDate; }

    /**
     * Add a line item to the invoice
     * @param lineItem line item to add
     */
    public void addLineItem(InvoiceLineItem lineItem) {
        lineItems.add(lineItem);
        calculateTotals();
    }

    /**
     * Remove a line item from the invoice
     * @param lineItem line item to remove
     */
    public void removeLineItem(InvoiceLineItem lineItem) {
        lineItems.remove(lineItem);
        calculateTotals();
    }

    /**
     * Calculate invoice totals (subtotal, tax, discount, total)
     */
    public void calculateTotals() {
        subtotal = 0.0;
        taxAmount = 0.0;
        discountAmount = 0.0;

        for (InvoiceLineItem item : lineItems) {
            subtotal += item.getTotalPrice();
            taxAmount += item.getTaxAmount();
            discountAmount += item.getDiscountAmount();
        }

        totalAmount = subtotal + taxAmount - discountAmount;

        // Ensure total doesn't go negative
        if (totalAmount < 0) {
            totalAmount = 0.0;
        }
    }

    /**
     * Process payment for the invoice
     * @param amount amount to pay
     * @param paymentMethod payment method used
     * @return true if payment successful, false otherwise
     */
    public boolean processPayment(double amount, PaymentMethod paymentMethod) {
        if (amount <= 0) {
            return false;
        }

        double remainingBalance = totalAmount - amountPaid;
        if (amount > remainingBalance) {
            amount = remainingBalance; // Don't overpay
        }

        this.paymentMethod = paymentMethod;
        this.amountPaid += amount;
        this.paymentDate = LocalDateTime.now();

        // Check if fully paid
        if (Math.abs(this.amountPaid - totalAmount) < 0.01) { // Account for floating point precision
            this.isPaid = true;
        }

        return true;
    }

    /**
     * Get remaining balance to be paid
     * @return remaining balance
     */
    public double getRemainingBalance() {
        return Math.max(0, totalAmount - amountPaid);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", lineItems=" + lineItems +
                ", subtotal=" + subtotal +
                ", taxAmount=" + taxAmount +
                ", discountAmount=" + discountAmount +
                ", totalAmount=" + totalAmount +
                ", paymentMethod=" + paymentMethod +
                ", isPaid=" + isPaid +
                ", amountPaid=" + amountPaid +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
