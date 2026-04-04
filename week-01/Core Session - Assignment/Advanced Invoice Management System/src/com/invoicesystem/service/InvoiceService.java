package com.invoicesystem.service;

import com.invoicesystem.model.Customer;
import com.invoicesystem.model.Invoice;
import com.invoicesystem.enums.PaymentMethod;
import com.invoicesystem.model.InvoiceLineItem;
import com.invoicesystem.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing invoice operations
 */
public class InvoiceService {
    private List<Invoice> invoices;
    private ProductService productService;
    private CustomerService customerService;

    public InvoiceService(ProductService productService, CustomerService customerService) {
        this.invoices = new ArrayList<>();
        this.productService = productService;
        this.customerService = customerService;
    }

    /**
     * Create a new invoice for a customer
     * @param customerId ID of the customer
     * @return new invoice or null if customer not found
     */
    public Invoice createInvoice(String customerId) {
        Optional<Customer> customerOpt = customerService.findCustomerById(customerId);
        if (customerOpt.isEmpty()) {
            return null;
        }

        Customer customer = customerOpt.get();
        Invoice invoice = new Invoice(customer.getId(), customer.getName());
        invoices.add(invoice);
        return invoice;
    }

    /**
     * Add a product to an invoice
     * @param invoiceNumber invoice number
     * @param productId product ID
     * @param quantity quantity to add
     * @param customerType customer type for pricing
     * @return true if successful
     */
    public boolean addProductToInvoice(String invoiceNumber, String productId, int quantity,
                                       com.invoicesystem.enums.CustomerType customerType) {
        Optional<Invoice> invoiceOpt = findInvoiceByNumber(invoiceNumber);
        Optional<Product> productOpt = productService.findProductById(productId);

        if (invoiceOpt.isEmpty() || productOpt.isEmpty() || quantity <= 0) {
            return false;
        }

        Invoice invoice = invoiceOpt.get();
        Product product = productOpt.get();

        // Check if we have enough stock
        if (!productService.sellProduct(productId, quantity)) {
            return false; // Not enough stock
        }

        // Get price for customer type
        double unitPrice = productService.getPriceForCustomerType(productId, customerType);

        // Determine discount based on quantity and customer type
        double discountPercentage = calculateDiscountPercentage(quantity, customerType);

        // Create line item
        InvoiceLineItem lineItem = new InvoiceLineItem(
                product.getId(),
                product.getName(),
                quantity,
                unitPrice,
                discountPercentage,
                product.getTaxType()
        );

        invoice.addLineItem(lineItem);

        // Add to customer's purchase history
        customerService.addPurchaseToHistory(invoice.getCustomerId(), invoice.getInvoiceNumber());

        return true;
    }

    /**
     * Calculate discount percentage based on quantity and customer type
     * @param quantity quantity of items
     * @param customerType customer type
     * @return discount percentage
     */
    private double calculateDiscountPercentage(int quantity, com.invoicesystem.enums.CustomerType customerType) {
        double discount = 0.0;

        // Bulk discount based on quantity
        if (quantity >= 10) {
            discount += 5.0; // 5% bulk discount
        } else if (quantity >= 5) {
            discount += 2.0; // 2% bulk discount
        }

        // Additional discount based on customer type
        switch (customerType) {
            case REGULAR:
                // No additional discount
                break;
            case PREMIUM:
                discount += 5.0; // Additional 5% for premium
                break;
            case CORPORATE:
                discount += 10.0; // Additional 10% for corporate
                break;
        }

        return discount;
    }

    /**
     * Find invoice by number
     * @param invoiceNumber invoice number
     * @return optional containing invoice if found
     */
    public Optional<Invoice> findInvoiceByNumber(String invoiceNumber) {
        return invoices.stream()
                .filter(i -> i.getInvoiceNumber().equals(invoiceNumber))
                .findFirst();
    }

    /**
     * Find invoices by customer ID
     * @param customerId customer ID
     * @return list of invoices for customer
     */
    public List<Invoice> findInvoicesByCustomerId(String customerId) {
        return invoices.stream()
                .filter(i -> i.getCustomerId().equals(customerId))
                .toList();
    }

    /**
     * Find invoices by date range
     * @param startDate start date (inclusive)
     * @param endDate end date (inclusive)
     * @return list of invoices in date range
     */
    public List<Invoice> findInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return invoices.stream()
                .filter(i -> !i.getInvoiceDate().isBefore(startDate) && !i.getInvoiceDate().isAfter(endDate))
                .toList();
    }

    /**
     * Process payment for an invoice
     * @param invoiceNumber invoice number
     * @param amount amount to pay
     * @param paymentMethod payment method
     * @return true if payment successful
     */
    public boolean processPayment(String invoiceNumber, double amount, PaymentMethod paymentMethod) {
        return findInvoiceByNumber(invoiceNumber)
                .map(invoice -> invoice.processPayment(amount, paymentMethod))
                .orElse(false);
    }

    /**
     * Get all invoices
     * @return list of all invoices
     */
    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices);
    }

    /**
     * Get paid invoices
     * @return list of paid invoices
     */
    public List<Invoice> getPaidInvoices() {
        return invoices.stream()
                .filter(Invoice::isPaid)
                .toList();
    }

    /**
     * Get unpaid invoices
     * @return list of unpaid invoices
     */
    public List<Invoice> getUnpaidInvoices() {
        return invoices.stream()
                .filter(i -> !i.isPaid())
                .toList();
    }

    /**
     * Get total revenue (sum of all paid invoice amounts)
     * @return total revenue
     */
    public double getTotalRevenue() {
        return invoices.stream()
                .filter(Invoice::isPaid)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    /**
     * Get total outstanding amount (sum of unpaid invoice balances)
     * @return total outstanding amount
     */
    public double getTotalOutstanding() {
        return invoices.stream()
                .filter(i -> !i.isPaid())
                .mapToDouble(Invoice::getRemainingBalance)
                .sum();
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}