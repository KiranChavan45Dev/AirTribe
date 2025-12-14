package com.airtribe.core.invoice.management.customer;

import com.airtribe.core.invoice.management.invoice.Invoice;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Customer implements Serializable {
    protected Long id;
    protected String name;
    protected String email;
    protected String phone;
    protected String address;
    protected List<Invoice> purchaseHistory;
    protected LocalDate registrationDate;

    public Customer() {
    }

    public Customer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.purchaseHistory = purchaseHistory;
        this.registrationDate = registrationDate;
    }

    public Customer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.purchaseHistory = purchaseHistory;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Invoice> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<Invoice> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Pattern.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (!Pattern.matches("^\\+?[0-9]{7,15}$", phone)) {
            throw new IllegalArgumentException("Invalid phone number: " + phone);
        }
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", purchaseHistory=" + purchaseHistory +
                '}';
    }
}
