package com.libms.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.libms.model.Book;

public class Inventory {
    private static final Logger logger = Logger.getLogger(Inventory.class.getName());
    private final Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        if (books.containsKey(book.getIspn())) {
            logger.warning("Book with ISPN " + book.getIspn() + " already exists in inventory.");
        } else {
            books.put(book.getIspn(), book);
            logger.info("Book added: " + book);
        }
    }

    public void removeBook(String ispn) {
        if (books.containsKey(ispn)) {
            Book removedBook = books.remove(ispn);
            logger.info("Book removed: " + removedBook);
        } else {
            logger.warning("No book found with ISPN " + ispn);
        }
    }

    public Optional<Book> getBook(String ispn) {
        return Optional.of(books.get(ispn));
    }

    public List<Book> searchBooksByTitle(String title) {
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String author) {
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return List.copyOf(books.values());
    }

    public boolean isBookAvailable(String ispn) {
        Book book = books.get(ispn);
        return book != null && !book.isBorrowed();
    }

    public long getTotalBooks() {
        return books.size();
    }

     public List<Book> availableBooks() {
        return books.values().stream().filter(b -> !b.isBorrowed()).collect(Collectors.toList());
    }
}
