package com.airtribe.core.invoice.management.manager;

import com.airtribe.core.invoice.management.payment.Payment;
import com.airtribe.core.invoice.management.invoice.PaymentMethod;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all payment operations: recording, searching, listing, and summaries.
 */
public class PaymentManager {

    private final List<Payment> payments = new ArrayList<>();

    // --- Record a New Payment ---
    public void recordPayment(Payment payment) {
        payments.add(payment);
        System.out.println("Payment recorded: " + payment.getPaymentId() + " | ₹" + payment.getAmountPaid());
    }

    // --- Create and Record Payment ---
    public Payment createAndRecordPayment(PaymentMethod method, double amount) {
        String id = "PAY-" + System.currentTimeMillis();
        Payment payment = new Payment(id, method, amount, LocalDateTime.now());
        recordPayment(payment);
        return payment;
    }

    // --- Get All Payments ---
    public List<Payment> getAllPayments() {
        return payments;
    }

    // --- Search by Payment ID ---
    public Payment searchById(String id) {
        return payments.stream()
                .filter(p -> p.getPaymentId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    // --- Filter by Method ---
    public List<Payment> searchByMethod(PaymentMethod method) {
        return payments.stream()
                .filter(p -> p.getMethod() == method)
                .collect(Collectors.toList());
    }

    // --- Filter by Date Range ---
    public List<Payment> searchByDateRange(LocalDateTime start, LocalDateTime end) {
        return payments.stream()
                .filter(p -> !p.getDate().isBefore(start) && !p.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    // --- Total Payments Collected ---
    public double getTotalCollected() {
        return payments.stream()
                .mapToDouble(Payment::getAmountPaid)
                .sum();
    }

    // --- Summary by Method ---
    public void printPaymentSummaryByMethod() {
        System.out.println("\n=== PAYMENT SUMMARY BY METHOD ===");
        Map<PaymentMethod, Double> summary = payments.stream()
                .collect(Collectors.groupingBy(
                        Payment::getMethod,
                        Collectors.summingDouble(Payment::getAmountPaid)
                ));

        summary.forEach((method, total) ->
                System.out.printf("%-15s : ₹%.2f%n", method, total));
    }

    // --- Print All Payments ---
    public void printAllPayments() {
        System.out.println("\n=== PAYMENT RECORDS ===");
        if (payments.isEmpty()) {
            System.out.println("No payments recorded.");
            return;
        }

        payments.forEach(p ->
                System.out.printf("ID: %-15s | Method: %-10s | Amount: ₹%-8.2f | Date: %s%n",
                        p.getPaymentId(),
                        p.getMethod(),
                        p.getAmountPaid(),
                        p.getDate())
        );
    }
}
