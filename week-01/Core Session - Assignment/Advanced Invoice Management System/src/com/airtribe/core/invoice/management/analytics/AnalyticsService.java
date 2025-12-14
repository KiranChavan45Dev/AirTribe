package com.airtribe.core.invoice.management.analytics;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.Invoice;
import com.airtribe.core.invoice.management.invoice.InvoiceItem;
import com.airtribe.core.invoice.management.product.Product;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    /**
     * Compute total purchase value of a customer from their purchase history
     */
    private static double calculateLifetimeValue(Customer customer) {
        if (customer.getPurchaseHistory() == null) return 0.0;

        return customer.getPurchaseHistory().stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    /**
     * Top 5 customers by total purchase
     */
    public static List<Customer> topCustomers(List<Customer> customers) {
        return customers.stream()
                .sorted(Comparator.comparingDouble(AnalyticsService::calculateLifetimeValue).reversed())
                .limit(5)
                .toList();
    }

    /**
     * Average order value by customer type (Regular, Premium, Corporate)
     */
    public static Map<String, Double> averageOrderValueByType(List<Customer> customers) {
        return customers.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getClass().getSimpleName(),
                        Collectors.averagingDouble(AnalyticsService::calculateLifetimeValue)
                ));
    }

    /**
     * Group sales by product category and month
     */
    public static Map<String, Map<Integer, Double>> salesByCategoryAndMonth(List<Invoice> invoices) {
        return invoices.stream()
                .flatMap(inv -> inv.getItems().stream()
                        .map(item -> Map.entry(
                                item.getProduct().getCategory().toString() + "-" + inv.getDate().getMonthValue(),
                                item.getTotalPrice()
                        ))
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)
                ))
                .entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getKey().split("-")[0],
                        Collectors.toMap(
                                e -> Integer.parseInt(e.getKey().split("-")[1]),
                                Map.Entry::getValue
                        )
                ));
    }



    /**
     * Low stock alerts
     */
    public static List<Product> lowStockProducts(List<Product> products) {
        return products.stream()
                .filter(p -> p.getStockQuantity() <= p.getReorderLevel())
                .toList();
    }

    /**
     * Filter invoices by date range and payment status
     */
    public static List<Invoice> filterInvoices(List<Invoice> invoices, LocalDate start, LocalDate end, String paymentStatus) {
        return invoices.stream()
                .filter(inv -> !inv.getDate().toLocalDate().isBefore(start) && !inv.getDate().toLocalDate().isAfter(end))
                .filter(inv -> inv.getPaymentStatus().toString().equalsIgnoreCase(paymentStatus))
                .toList();
    }
}
