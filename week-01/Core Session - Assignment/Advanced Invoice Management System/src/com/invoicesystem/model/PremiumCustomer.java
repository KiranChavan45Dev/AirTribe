package com.invoicesystem.model;

import com.invoicesystem.enums.CustomerType;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Premium customer with discount percentage and loyalty points
 */
public class PremiumCustomer extends Customer implements Serializable {
    private double discountPercentage;
    private int loyaltyPoints;

    public PremiumCustomer(String id, String name, String email, String phone, String address,
                           LocalDate registrationDate, double discountPercentage, int loyaltyPoints) {
        super(id, name, email, phone, address, registrationDate, CustomerType.PREMIUM);
        this.discountPercentage = discountPercentage;
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public double calculateLifetimeValue() {
        // Simplified calculation - in a real system this would be based on actual purchase history
        // For now, we'll use loyalty points as a proxy for lifetime value
        return loyaltyPoints * 0.1; // Assuming 1 loyalty point = $0.10 value
    }

    @Override
    public String toString() {
        return "PremiumCustomer{" +
                "discountPercentage=" + discountPercentage +
                ", loyaltyPoints=" + loyaltyPoints +
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