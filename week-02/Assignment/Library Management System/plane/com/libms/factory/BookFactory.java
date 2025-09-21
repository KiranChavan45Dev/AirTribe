package com.libms.factory;

import com.libms.model.Book;

public final class BookFactory {
    private BookFactory() {
        // Private constructor to prevent instantiation
    }

    public static Book createBook(String ispn, String title, String author, int publicationYear) {
        return new Book(ispn, title, author, publicationYear);
    }
}
