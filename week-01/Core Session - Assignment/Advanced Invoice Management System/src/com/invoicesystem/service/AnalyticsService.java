package com.invoicesystem.service;

import com.invoicesystem.enums.CustomerType;
import com.invoicesystem.enums.ProductCategory;
import com.invoicesystem.model.Customer;
import com.invoicesystem.model.Invoice;
import com.invoicesystem.model.InvoiceLineItem;
import com.invoicesystem.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    private InvoiceService invoiceService;
    private CustomerService customerService;
    private ProductService productService;

    public List<Invoice> getInvoicesByDateRange(LocalDateTime start, LocalDateTime end) {
        return invoiceService.getAllInvoices().stream()
                .filter(inv -> !inv.getInvoiceDate().isBefore(start)
                        && !inv.getInvoiceDate().isAfter(end))
                .toList();
    }

    public List<Map.Entry<String, Integer>> getBestSellingProducts() {
        return invoiceService.getAllInvoices().stream()
                .flatMap(inv -> inv.getLineItems().stream())
                .collect(Collectors.groupingBy(
                        InvoiceLineItem::getProductId,
                        Collectors.summingInt(InvoiceLineItem::getQuantity)
                ))
                .entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .toList();
    }

    public Map<String, Long> getCustomerPurchaseFrequency() {
        return invoiceService.getAllInvoices().stream()
                .collect(Collectors.groupingBy(
                        Invoice::getCustomerId,
                        Collectors.counting()
                ));
    }

    public Map<ProductCategory, Double> getRevenueByCategory() {
        return invoiceService.getAllInvoices().stream()
                .flatMap(inv -> inv.getLineItems().stream())
                .collect(Collectors.groupingBy(
                        item -> productService.findProductById(item.getProductId())
                                .map(Product::getCategory)
                                .orElse(ProductCategory.ELECTRONICS),
                        Collectors.summingDouble(InvoiceLineItem::getTotalPrice)
                ));
    }

    public Map<CustomerType, Double> getAverageOrderValueByCustomerType() {
        return invoiceService.getAllInvoices().stream()
                .filter(Invoice::isPaid)
                .collect(Collectors.groupingBy(
                        invoice -> customerService
                                .findCustomerById(invoice.getCustomerId())
                                .map(Customer::getType)
                                .orElse(CustomerType.REGULAR),
                        Collectors.averagingDouble(Invoice::getTotalAmount)
                ));
    }

    public List<Map.Entry<String, Double>> getTop5Customers() {
        return invoiceService.getAllInvoices().stream()
                .filter(Invoice::isPaid)
                .collect(Collectors.groupingBy(
                        Invoice::getCustomerId,
                        Collectors.summingDouble(Invoice::getTotalAmount)
                ))
                .entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .toList();
    }


    public AnalyticsService(InvoiceService invoiceService,
                            CustomerService customerService,
                            ProductService productService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.productService = productService;
    }

    public Map<String, Double> getMonthlyRevenue() {
        return invoiceService.getAllInvoices().stream()
                .filter(Invoice::isPaid)
                .collect(Collectors.groupingBy(
                        inv -> inv.getInvoiceDate().getYear() + "-" +
                                inv.getInvoiceDate().getMonthValue(),
                        Collectors.summingDouble(Invoice::getTotalAmount)
                ));
    }
}
