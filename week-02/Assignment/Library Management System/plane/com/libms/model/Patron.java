package com.libms.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patron {
    private final String patronId;
    private String name;
    private String email;
    private String phoneNumber;


    private List<String> borrowHistroy = new ArrayList<>(); // List of ISPNs of borrowed books

    public Patron(String patronId, String name, String email, String phoneNumber) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getPatronId() {
        return patronId;
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
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addToHistory(String isbn) {
        borrowHistroy.add(isbn);
    }

    public List<String> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowHistroy);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", historySize='" + borrowHistroy.size() + '\'' +
                '}';
    }
}
