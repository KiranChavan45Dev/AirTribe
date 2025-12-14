package com.airtribe.core.invoice.management.manager;

import com.airtribe.core.invoice.management.customer.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerManager {
    private final List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer customer) {
        customer.setRegistrationDate(LocalDate.now());
        customers.add(customer);
        System.out.println("Customer added: " + customer.getName());
    }

    public void updateCustomer(Long id, String newEmail, String newPhone, String newAddress) {
        Optional<Customer> found = customers.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (found.isPresent()) {
            Customer c = found.get();
            if (newEmail != null) c.setEmail(newEmail);
            if (newPhone != null) c.setPhone(newPhone);
            if (newAddress != null) c.setAddress(newAddress);
            System.out.println("Customer updated: " + c.getName());
        } else {
            System.out.println("Customer ID not found: " + id);
        }
    }

    public void searchCustomer(String keyword) {
        customers.stream()
                .filter(c -> c.getName().equalsIgnoreCase(keyword)
                        || c.getEmail().equalsIgnoreCase(keyword)
                        || String.valueOf(c.getId()).equals(keyword))
                .forEach(System.out::println);
    }

    public void viewCustomerHistory(Long id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresentOrElse(
                        c -> {
                            System.out.println("=== Purchase History for " + c.getName() + " ===");
                            if (c.getPurchaseHistory() == null || c.getPurchaseHistory().isEmpty()) {
                                System.out.println("No purchases yet.");
                            } else {
                                c.getPurchaseHistory().forEach(System.out::println);
                            }
                        },
                        () -> System.out.println("Customer not found.")
                );
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }
}
