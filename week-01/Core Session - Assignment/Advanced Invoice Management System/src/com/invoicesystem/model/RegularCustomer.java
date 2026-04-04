package com.invoicesystem.model;

import com.invoicesystem.enums.CustomerType;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Regular customer with standard pricing
 */
public class RegularCustomer extends Customer implements Serializable {
    public RegularCustomer(String id, String name, String email, String phone, String address,
                           LocalDate registrationDate) {
        super(id, name, email, phone, address, registrationDate, CustomerType.REGULAR);
    }

    @Override
    public double getDiscountPercentage() {
        return 0.0; // No discount for regular customers
    }

    @Override
    public double calculateLifetimeValue() {
        // Simplified calculation - in a real system this would be based on actual purchase history
        return 0.0; // Placeholder
    }
}