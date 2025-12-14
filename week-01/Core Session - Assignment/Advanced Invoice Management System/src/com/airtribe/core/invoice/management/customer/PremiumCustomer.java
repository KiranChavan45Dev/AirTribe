package com.airtribe.core.invoice.management.customer;

import com.airtribe.core.invoice.management.invoice.Invoice;

import java.time.LocalDate;
import java.util.List;

public class PremiumCustomer extends Customer{
    private Double discountPercentage; // e.g., 10 for 10%
    private Integer loyaltyPoints;

    public PremiumCustomer(Double discountPercentage, Integer loyaltyPoints) {
        this.discountPercentage = discountPercentage;
        this.loyaltyPoints = loyaltyPoints;
    }

    public PremiumCustomer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory, LocalDate registrationDate, Double discountPercentage, Integer loyaltyPoints) {
        super(id, name, email, phone, address, purchaseHistory, registrationDate);
        this.discountPercentage = discountPercentage;
        this.loyaltyPoints = loyaltyPoints;
    }

    public PremiumCustomer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory, Double discountPercentage, Integer loyaltyPoints) {
        super(id, name, email, phone, address, purchaseHistory);
        this.discountPercentage = discountPercentage;
        this.loyaltyPoints = loyaltyPoints;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return "PremiumCustomer{" +
                "discountPercentage=" + discountPercentage +
                ", loyaltyPoints=" + loyaltyPoints +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", purchaseHistory=" + purchaseHistory +
                '}';
    }
}
