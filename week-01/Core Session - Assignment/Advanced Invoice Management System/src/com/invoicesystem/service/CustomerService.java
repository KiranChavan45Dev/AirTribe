package com.invoicesystem.service;

import com.invoicesystem.model.CorporateCustomer;
import com.invoicesystem.model.Customer;
import com.invoicesystem.model.PremiumCustomer;
import com.invoicesystem.enums.CustomerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing customer operations
 */
public class CustomerService {
    private List<Customer> customers;

    public CustomerService() {
        this.customers = new ArrayList<>();
    }

    /**
     * Add a new customer
     * @param customer customer to add
     * @return true if successful
     */
    public boolean addCustomer(Customer customer) {
        if (customer == null || findCustomerById(customer.getId()).isPresent()) {
            return false;
        }
        customers.add(customer);
        return true;
    }

    /**
     * Find customer by ID
     * @param id customer ID
     * @return optional containing customer if found
     */
    public Optional<Customer> findCustomerById(String id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    /**
     * Find customers by name (partial match)
     * @param name name to search for
     * @return list of matching customers
     */
    public List<Customer> findCustomersByName(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>();
        }
        return customers.stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    /**
     * Find customers by email (partial match)
     * @param email email to search for
     * @return list of matching customers
     */
    public List<Customer> findCustomersByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        return customers.stream()
                .filter(c -> c.getEmail().toLowerCase().contains(email.toLowerCase()))
                .toList();
    }

    /**
     * Update customer information
     * @param id customer ID
     * @param updatedCustomer updated customer data
     * @return true if successful
     */
    public boolean updateCustomer(String id, Customer updatedCustomer) {
        Optional<Customer> existingCustomerOpt = findCustomerById(id);
        if (existingCustomerOpt.isEmpty() || updatedCustomer == null) {
            return false;
        }

        Customer existingCustomer = existingCustomerOpt.get();
        // Update fields while preserving ID and type
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setRegistrationDate(updatedCustomer.getRegistrationDate());

        // For premium and corporate customers, update specific fields
        if (updatedCustomer instanceof PremiumCustomer && existingCustomer instanceof PremiumCustomer) {
            PremiumCustomer updatedPremium = (PremiumCustomer) updatedCustomer;
            PremiumCustomer existingPremium = (PremiumCustomer) existingCustomer;
            existingPremium.setDiscountPercentage(updatedPremium.getDiscountPercentage());
            existingPremium.setLoyaltyPoints(updatedPremium.getLoyaltyPoints());
        } else if (updatedCustomer instanceof CorporateCustomer && existingCustomer instanceof CorporateCustomer) {
            CorporateCustomer updatedCorp = (CorporateCustomer) updatedCustomer;
            CorporateCustomer existingCorp = (CorporateCustomer) existingCustomer;
            existingCorp.setCreditLimit(updatedCorp.getCreditLimit());
            existingCorp.setPaymentTerms(updatedCorp.getPaymentTerms());
            existingCorp.setTaxExempt(updatedCorp.isTaxExempt());
        }

        return true;
    }

    /**
     * Get all customers
     * @return list of all customers
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    /**
     * Get customers by type
     * @param type customer type
     * @return list of customers of specified type
     */
    public List<Customer> getCustomersByType(CustomerType type) {
        return customers.stream()
                .filter(c -> c.getType().equals(type))
                .toList();
    }

    /**
     * Calculate lifetime value for a customer
     * @param id customer ID
     * @return lifetime value or 0 if customer not found
     */
    public double calculateLifetimeValue(String id) {
        return findCustomerById(id)
                .map(Customer::calculateLifetimeValue)
                .orElse(0.0);
    }

    /**
     * Add purchase to customer history
     * @param customerId customer ID
     * @param invoiceId invoice ID
     * @return true if successful
     */
    public boolean addPurchaseToHistory(String customerId, String invoiceId) {
        return findCustomerById(customerId)
                .map(customer -> {
                    customer.addToPurchaseHistory(invoiceId);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Validate email format (basic validation)
     * @param email email to validate
     * @return true if valid format
     */
    public boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Basic email validation - contains @ and . after @
        int atIndex = email.indexOf('@');
        int dotIndex = email.lastIndexOf('.');
        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length() - 1;
    }

    /**
     * Validate phone format (basic validation)
     * @param phone phone to validate
     * @return true if valid format (contains only digits and common phone chars)
     */
    public boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        // Allow digits, spaces, hyphens, parentheses, and plus sign
        return phone.matches("[\\d\\s\\-\\(\\)\\+]+");
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}