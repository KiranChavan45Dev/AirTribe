package com.libms.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.libms.factory.PatronFactory;
import com.libms.model.Book;
import com.libms.model.Patron;
import com.libms.notify.NotificationService;
import com.libms.recommend.RecommendationService;

public class Library {
    private static final Logger LOG = Logger.getLogger(Library.class.getName());

    private final Map<String, Branch> branches = new HashMap<>();
    private final Map<String, Patron> patrons = new HashMap<>();

    public Branch createBranch(String id) {
        Branch branch = new Branch(id);
        branches.put(id, branch);
        LOG.info("Created branch " + id);
        return branch;
    }

    public Optional<Branch> getBranch(String id) {
        return Optional.of(branches.get(id));
    }

    public Patron addPatron(String id, String name) {
        Patron patron = PatronFactory.createPatron(id, name, id, name);
        patrons.put(id, patron);
        LOG.info("Created patron " + name + " with ID " + id);
        return patron;
    }

    public Patron getPatron(String id) {
        return patrons.get(id);
    }


     public boolean checkout(String branchId, String isbn, String patronId) {
        Branch branch = branches.get(branchId);
        Patron patron = patrons.get(patronId);
        if (branch == null || patron == null) return false;
        return branch.checkoutBook(isbn, patron);
    }

    public boolean returnBook(String branchId, String isbn) {
        Branch branch = branches.get(branchId);
        if (branch == null) return false;
        return branch.returnBook(isbn);
    }

    public void reserveBook(String branchId, String isbn, String patronId) {
        Branch branch = branches.get(branchId);
        Patron patron = patrons.get(patronId);
        if (branch == null || patron == null) return;
        NotificationService ns = new NotificationService(patronId, patron.getEmail());
        branch.getReservation().reserve(isbn, ns);
    }

    public List<Book> recommend(String branchId, String patronId, int limit) {
        Branch branch = branches.get(branchId);
        Patron patron = patrons.get(patronId);
        if (branch == null || patron == null) return Collections.emptyList();
        RecommendationService svc = new RecommendationService();
        return svc.recommend(patron, branch, limit);
    }
    public Collection<Branch> getAllBranches() {
        return branches.values();
    }
}
