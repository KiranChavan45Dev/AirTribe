package com.libms.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.libms.model.Book;
import com.libms.model.Patron;

public class Branch {
    private static final Logger logger = Logger.getLogger(Branch.class.getName());  
    private final String branchId;
    private Inventory inventory = new Inventory();

    // ISBN -> patronId currently holding
    private final Map<String, String> borrowed = new HashMap<>();

    // reservations
    private final Reservation reservation = new Reservation();

    public Branch(String branchId) {
        this.branchId = branchId;
    }
    public String getBranchId() {
        return branchId;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public Reservation getReservation() {
        return reservation;
    }

    public Map<String, String> getBorrowed() {
        return borrowed;
    }
    
    public boolean isBookBorrowed(String ispn) {
        return borrowed.containsKey(ispn);
    }

    public boolean checkoutBook(String ispn, Patron patron) {
        Book book = inventory.getBook(ispn).get();
        
        if(book == null) {
            logger.warning("Book with ISPN " + ispn + " does not exist in inventory.");
            return false;
        }
        if(book.isBorrowed()) {
            logger.warning("Book with ISPN " + ispn + " is already borrowed.");
            return false;
        }
        book.setBorrowed(true);
        borrowed.put(ispn, patron.getPatronId());
        patron.addToHistory(ispn);
        logger.info("Checked out " + ispn + " to " + patron.getPatronId());
        return true;
    }

    public boolean returnBook(String ispn){
        Book book = inventory.getBook(ispn).get();
        
        if(book == null) {
            logger.warning("Book with ISPN " + ispn + " does not exist in inventory.");
            return false;
        }
        if(!book.isBorrowed()) {
            logger.warning("Book with ISPN " + ispn + " is not currently borrowed.");
            return false;
        }
        book.setBorrowed(false);
        borrowed.remove(ispn);
        logger.info("Returned book with ISPN " + ispn);
        reservation.notify(ispn);
        return true;
    }

}
