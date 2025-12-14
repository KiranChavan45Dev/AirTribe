package com.airtribe.core.invoice.management.strategy.discount;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.InvoiceItem;

import java.util.List;

public class FixedDiscount implements DiscountStrategy{
    private Double discountAmount;

    public FixedDiscount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public Double applyDiscount(Double amount, Customer customer, List<InvoiceItem> items) {
        double discounted = amount - discountAmount;
        return discounted < 0 ? 0 : discounted; // prevent negative totals
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
