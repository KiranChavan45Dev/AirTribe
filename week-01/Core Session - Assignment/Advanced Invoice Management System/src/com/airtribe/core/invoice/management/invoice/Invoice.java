package com.airtribe.core.invoice.management.invoice;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.payment.PaymentStatus;
import com.airtribe.core.invoice.management.strategy.discount.DiscountStrategy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {
    private String invoiceNumber;
    private LocalDateTime date;
    private Customer customer;
    private List<InvoiceItem> items = new ArrayList<>();
    private Double subtotal = 0.0;
    private Double taxAmount = 0.0;
    private Double discountAmount = 0.0;
    private Double totalAmount = 0.0;
    private Double amountPaid = 0.0;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private TaxType taxType = TaxType.GST;

    // --- Constructors ---
    public Invoice(Customer customer) {
        this.invoiceNumber = generateInvoiceNumber();
        this.date = LocalDateTime.now();
        this.customer = customer;
    }

    // --- Utility: Generate unique invoice ID ---
    private String generateInvoiceNumber() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "INV-" + LocalDateTime.now().format(fmt);
    }

    // --- Add invoice line item ---
    public void addItem(InvoiceItem item) {
        items.add(item);
        recalculateTotals();
    }

    // --- Apply discount strategy ---
    public void applyDiscount(DiscountStrategy strategy,Customer customer,List<InvoiceItem> items) {
        double discount = strategy.applyDiscount(subtotal,customer,items);
        this.discountAmount = discount;
        recalculateTotals();
    }

    // --- Recalculate totals ---
    private void recalculateTotals() {
        subtotal = items.stream().mapToDouble(InvoiceItem::getTotalPrice).sum();
        taxAmount = subtotal * taxType.getTaxRate();
        totalAmount = subtotal + taxAmount - discountAmount;

        // Update payment status
        if (amountPaid >= totalAmount) {
            paymentStatus = PaymentStatus.PAID;
        } else if (amountPaid > 0) {
            paymentStatus = PaymentStatus.PARTIAL;
        } else {
            paymentStatus = PaymentStatus.PENDING;
        }
    }

    // --- Record payment ---
    public void recordPayment(double payment) {
        this.amountPaid += payment;
        recalculateTotals();
    }

    // --- Getters/Setters ---
    public String getInvoiceNumber() { return invoiceNumber; }
    public LocalDateTime getDate() { return date; }
    public Customer getCustomer() { return customer; }
    public List<InvoiceItem> getItems() { return items; }
    public Double getSubtotal() { return subtotal; }
    public Double getTaxAmount() { return taxAmount; }
    public Double getDiscountAmount() { return discountAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public Double getAmountPaid() { return amountPaid; }
    public Double getBalanceDue() { return totalAmount - amountPaid; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", date=" + date +
                ", customer=" + customer.getName() +
                ", subtotal=" + subtotal +
                ", taxType=" + taxType +
                ", taxAmount=" + taxAmount +
                ", discountAmount=" + discountAmount +
                ", totalAmount=" + totalAmount +
                ", amountPaid=" + amountPaid +
                ", balanceDue=" + getBalanceDue() +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
