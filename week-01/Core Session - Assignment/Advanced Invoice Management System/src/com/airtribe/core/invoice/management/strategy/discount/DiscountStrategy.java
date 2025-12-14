package com.airtribe.core.invoice.management.strategy.discount;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.InvoiceItem;

import java.util.List;

public interface DiscountStrategy {
    Double applyDiscount(Double amount, Customer customer, List<InvoiceItem> items);
}
