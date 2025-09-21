package com.libms.model;

public class Book {
    private final String ispn;
    private String title;
    private String author;
    private int publicationYear;

    private boolean isBorrowed;

    public Book(String ispn, String title, String author, int publicationYear) {
        this.ispn = ispn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isBorrowed = false;
    }

    public String getIspn() {
        return ispn;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
    public boolean isBorrowed() {
        return isBorrowed;
    }
    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "ispn='" + ispn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", isBorrowed=" + isBorrowed +
                '}';
    }

}
