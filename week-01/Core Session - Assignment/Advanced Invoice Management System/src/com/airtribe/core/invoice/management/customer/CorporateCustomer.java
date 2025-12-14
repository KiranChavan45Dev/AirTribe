package com.airtribe.core.invoice.management.customer;

import com.airtribe.core.invoice.management.invoice.Invoice;

import java.time.LocalDate;
import java.util.List;

public class CorporateCustomer extends Customer{
    private Double creditLimit;
    private String paymentTerms;
    private Boolean taxExempt;

    public CorporateCustomer(Double creditLimit, String paymentTerms, Boolean taxExempt) {
        this.creditLimit = creditLimit;
        this.paymentTerms = paymentTerms;
        this.taxExempt = taxExempt;
    }

    public CorporateCustomer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory, LocalDate registrationDate, Double creditLimit, String paymentTerms, Boolean taxExempt) {
        super(id, name, email, phone, address, purchaseHistory, registrationDate);
        this.creditLimit = creditLimit;
        this.paymentTerms = paymentTerms;
        this.taxExempt = taxExempt;
    }

    public CorporateCustomer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory, Double creditLimit, String paymentTerms, Boolean taxExempt) {
        super(id, name, email, phone, address, purchaseHistory);
        this.creditLimit = creditLimit;
        this.paymentTerms = paymentTerms;
        this.taxExempt = taxExempt;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public Boolean getTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(Boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    @Override
    public String toString() {
        return "CorporateCustomer{" +
                "creditLimit=" + creditLimit +
                ", paymentTerms='" + paymentTerms + '\'' +
                ", taxExempt=" + taxExempt +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", purchaseHistory=" + purchaseHistory +
                '}';
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }
}
