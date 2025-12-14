package com.airtribe.core.invoice.management.customer;

import com.airtribe.core.invoice.management.invoice.Invoice;

import java.time.LocalDate;
import java.util.List;

public class RegularCustomer extends Customer{
    public RegularCustomer() {
    }

    public RegularCustomer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory, LocalDate registrationDate) {
        super(id, name, email, phone, address, purchaseHistory, registrationDate);
    }

    public RegularCustomer(Long id, String name, String email, String phone, String address, List<Invoice> purchaseHistory) {
        super(id, name, email, phone, address, purchaseHistory);
    }

    @Override
    public String toString() {
        return "RegularCustomer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", purchaseHistory=" + purchaseHistory +
                '}';
    }
}
