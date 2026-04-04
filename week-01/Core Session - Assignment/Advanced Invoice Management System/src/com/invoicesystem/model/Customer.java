package com.invoicesystem.model;

import com.invoicesystem.enums.CustomerType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Customer class with common attributes and methods
 */
public abstract class Customer implements Serializable {
    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String address;
    protected LocalDate registrationDate;
    protected CustomerType type;
    protected List<String> purchaseHistory;

    public Customer(String id, String name, String email, String phone, String address,
                    LocalDate registrationDate, CustomerType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
        this.type = type;
        this.purchaseHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public CustomerType getType() { return type; }
    public void setType(CustomerType type) { this.type = type; }

    public List<String> getPurchaseHistory() { return purchaseHistory; }
    public void setPurchaseHistory(List<String> purchaseHistory) { this.purchaseHistory = purchaseHistory; }

    public void addToPurchaseHistory(String invoiceId) {
        purchaseHistory.add(invoiceId);
    }

    /**
     * Abstract method to calculate discount percentage based on customer type
     * @return discount percentage
     */
    public abstract double getDiscountPercentage();

    /**
     * Abstract method to calculate lifetime value
     * @return lifetime value
     */
    public abstract double calculateLifetimeValue();

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", registrationDate=" + registrationDate +
                ", type=" + type +
                '}';
    }
}