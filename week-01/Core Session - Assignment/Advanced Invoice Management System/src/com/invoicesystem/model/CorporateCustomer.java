package com.invoicesystem.model;

import com.invoicesystem.enums.CustomerType;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Corporate customer with credit limit and payment terms
 */
public class CorporateCustomer extends Customer implements Serializable {
    private double creditLimit;
    private int paymentTerms; // in days
    private boolean taxExempt;

    public CorporateCustomer(String id, String name, String email, String phone, String address,
                             LocalDate registrationDate, double creditLimit, int paymentTerms, boolean taxExempt) {
        super(id, name, email, phone, address, registrationDate, CustomerType.CORPORATE);
        this.creditLimit = creditLimit;
        this.paymentTerms = paymentTerms;
        this.taxExempt = taxExempt;
    }

    @Override
    public double getDiscountPercentage() {
        // Corporate customers get a standard discount based on credit limit
        return Math.min(creditLimit / 10000.0 * 5.0, 20.0); // Max 20% discount
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(int paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    @Override
    public double calculateLifetimeValue() {
        // Simplified calculation - in a real system this would be based on actual purchase history
        // For now, we'll use credit limit as a proxy for lifetime value potential
        return creditLimit * 0.5; // Assuming 50% of credit limit as lifetime value
    }

    @Override
    public String toString() {
        return "CorporateCustomer{" +
                "creditLimit=" + creditLimit +
                ", paymentTerms=" + paymentTerms +
                ", taxExempt=" + taxExempt +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", registrationDate=" + registrationDate +
                ", type=" + type +
                '}';
    }
}