package com.airtribe.core.invoice.management.strategy.discount;

import com.airtribe.core.invoice.management.customer.Customer;
import com.airtribe.core.invoice.management.invoice.InvoiceItem;

import java.util.List;

public class BulkDiscount implements DiscountStrategy{

    private Integer thresholdQuantity;
    private Double discountRate;

    public BulkDiscount(Integer thresholdQuantity, Double discountRate) {
        this.thresholdQuantity = thresholdQuantity;
        this.discountRate = discountRate;
    }

    @Override
    public Double applyDiscount(Double amount, Customer customer, List<InvoiceItem> items) {
        Integer totalQuantity = 0;
        for (InvoiceItem item : items) {
            totalQuantity += item.getQuantity();
        }

        if (totalQuantity >= thresholdQuantity) {
            return amount - (amount * discountRate);
        }
        return amount;
    }

    public Integer getThresholdQuantity() {
        return thresholdQuantity;
    }

    public void setThresholdQuantity(Integer thresholdQuantity) {
        this.thresholdQuantity = thresholdQuantity;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

}
