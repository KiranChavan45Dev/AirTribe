package com.airtribe.core.invoice.management.strategy.discount;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.InvoiceItem;

import java.util.List;

public class PercentageDiscount implements DiscountStrategy{
    private Double rate;

    public PercentageDiscount(Double rate) {
        this.rate = rate;
    }

    @Override
    public Double applyDiscount(Double amount, Customer customer, List<InvoiceItem> items) {
        return amount - (amount * rate);
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
