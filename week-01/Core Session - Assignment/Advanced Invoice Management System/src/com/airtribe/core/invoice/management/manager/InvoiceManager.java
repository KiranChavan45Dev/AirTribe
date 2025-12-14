package com.airtribe.core.invoice.management.manager;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.Invoice;
import com.airtribe.core.invoice.management.invoice.InvoiceItem;
import com.airtribe.core.invoice.management.invoice.PaymentMethod;
import com.airtribe.core.invoice.management.invoice.TaxType;
import com.airtribe.core.invoice.management.payment.PaymentStatus;
import com.airtribe.core.invoice.management.strategy.discount.DiscountStrategy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages creation, retrieval, and processing of invoices.
 */
public class InvoiceManager {
    private final List<Invoice> invoices = new ArrayList<>();

    // --- Create new invoice ---
    public Invoice createInvoice(Customer customer) {
        Invoice invoice = new Invoice(customer);
        invoices.add(invoice);
        return invoice;
    }

    // --- Add item to invoice ---
    public void addItemToInvoice(Invoice invoice, InvoiceItem item) {
        invoice.addItem(item);
    }

    // --- Apply discount ---
    public void applyDiscount(Invoice invoice, DiscountStrategy strategy) {
        invoice.applyDiscount(strategy, invoice.getCustomer(), invoice.getItems());
    }

    // --- Record payment ---
    public void recordPayment(Invoice invoice, double amount, PaymentMethod method) {
        invoice.recordPayment(amount);
        System.out.println("Payment of ₹" + amount + " received via " + method + ".");
    }

    // --- Set tax type ---
    public void setTaxType(Invoice invoice, TaxType taxType) {
        invoice.setTaxType(taxType);
    }

    // --- Search invoice by number ---
    public Invoice searchByInvoiceNumber(String invoiceNumber) {
        return invoices.stream()
                .filter(inv -> inv.getInvoiceNumber().equalsIgnoreCase(invoiceNumber))
                .findFirst()
                .orElse(null);
    }

    // --- Search invoices by customer ---
    public List<Invoice> searchByCustomer(Customer customer) {
        return invoices.stream()
                .filter(inv -> inv.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    // --- View all invoices ---
    public List<Invoice> getAllInvoices() {
        return invoices;
    }

    // --- Get unpaid invoices ---
    public List<Invoice> getUnpaidInvoices() {
        return invoices.stream()
                .filter(inv -> inv.getPaymentStatus() != PaymentStatus.PAID)
                .collect(Collectors.toList());
    }

    // --- Delete an invoice ---
    public boolean deleteInvoice(String invoiceNumber) {
        return invoices.removeIf(inv -> inv.getInvoiceNumber().equalsIgnoreCase(invoiceNumber));
    }

    // --- Display summary report ---
    public void printInvoiceSummary() {
        System.out.println("\n=== INVOICE SUMMARY ===");
        invoices.forEach(inv -> System.out.printf(
                "%s | %s | Customer: %s | Total: ₹%.2f | Status: %s%n",
                inv.getInvoiceNumber(),
                inv.getDate(),
                inv.getCustomer().getName(),
                inv.getTotalAmount(),
                inv.getPaymentStatus()
        ));
    }
}
