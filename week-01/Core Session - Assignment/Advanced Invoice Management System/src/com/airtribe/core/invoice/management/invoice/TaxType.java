package com.airtribe.core.invoice.management.invoice;

public enum TaxType {
    GST(0.18),          // 18% GST
    VAT(0.12),          // 12% VAT
    SERVICE_TAX(0.10);  // 10% Service Tax

    private final double taxRate;

    TaxType(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTaxRate() {
        return taxRate;
    }
}
