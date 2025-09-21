package com.libms.factory;

import com.libms.model.Patron;

public final class PatronFactory {
    private PatronFactory() {
        // Private constructor to prevent instantiation
    }

    public static Patron createPatron(String patronId, String name, String email, String phoneNumber) {
        return new Patron(patronId, name, email, phoneNumber);
    }
    
}
