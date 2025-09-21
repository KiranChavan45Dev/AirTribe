package com.libms;

import com.libms.core.Branch;
import com.libms.core.Library;
import com.libms.factory.BookFactory;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Library lib = new Library();
        Branch b1 = lib.createBranch("central");

        // preload sample data
        b1.getInventory().addBook(BookFactory.createBook("978-013", "Effective Java", "Joshua Bloch", 2018));
        b1.getInventory().addBook(BookFactory.createBook("978-020", "Design Patterns", "Erich Gamma", 1994));
        b1.getInventory().addBook(BookFactory.createBook("978-012", "Clean Code", "Robert C. Martin", 2008));
        lib.addPatron("p1", "Kiran");
        lib.addPatron("p2", "Maya");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            printMenu();
            System.out.print("Enter choice: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input, enter a number!");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1: // List books
                        Branch br = lib.getBranch("central").orElse(null);
                        if (br == null) {
                            System.out.println("No branch central");
                        } else {
                            br.getInventory().getAllBooks().forEach(System.out::println);
                        }
                        break;

                    case 2: // Search by title
                        System.out.print("Enter title: ");
                        String title = sc.nextLine();
                        lib.getBranch("central")
                                .ifPresent(branch -> branch.getInventory()
                                        .searchBooksByTitle(title)
                                        .forEach(System.out::println));
                        break;

                    case 3: // Search by author
                        System.out.print("Enter author: ");
                        String author = sc.nextLine();
                        lib.getBranch("central")
                                .ifPresent(branch -> branch.getInventory()
                                        .searchBooksByAuthor(author)
                                        .forEach(System.out::println));
                        break;

                    case 4: // Checkout
                        System.out.print("Enter branch: ");
                        String branch = sc.nextLine();
                        System.out.print("Enter ISBN: ");
                        String isbn = sc.nextLine();
                        System.out.print("Enter patron ID: ");
                        String patron = sc.nextLine();
                        boolean ok = lib.checkout(branch, isbn, patron);
                        System.out.println(ok ? "Checked out" : "Checkout failed");
                        break;

                    case 5: // Return
                        System.out.print("Enter branch: ");
                        branch = sc.nextLine();
                        System.out.print("Enter ISBN: ");
                        isbn = sc.nextLine();
                        ok = lib.returnBook(branch, isbn);
                        System.out.println(ok ? "Returned" : "Return failed");
                        break;

                    case 6: // Reserve
                        System.out.print("Enter branch: ");
                        branch = sc.nextLine();
                        System.out.print("Enter ISBN: ");
                        isbn = sc.nextLine();
                        System.out.print("Enter patron ID: ");
                        patron = sc.nextLine();
                        lib.reserveBook(branch, isbn, patron);
                        System.out.println("Reserved");
                        break;

                    case 7: // Recommend
                        System.out.print("Enter branch: ");
                        branch = sc.nextLine();
                        System.out.print("Enter patron ID: ");
                        patron = sc.nextLine();
                        var recs = lib.recommend(branch, patron, 5);
                        recs.forEach(System.out::println);
                        break;

                    case 8: // Add Book
                        System.out.print("Enter branch: ");
                        branch = sc.nextLine();
                        System.out.print("Enter ISBN: ");
                        isbn = sc.nextLine();
                        System.out.print("Enter Title: ");
                        title = sc.nextLine();
                        System.out.print("Enter Author: ");
                        author = sc.nextLine();
                        System.out.print("Enter Year: ");
                        int year = sc.nextInt();
                        sc.nextLine();
                        Optional<Branch> ob = lib.getBranch(branch);
                        if (ob.isPresent()) {
                            ob.get().getInventory().addBook(BookFactory.createBook(isbn, title, author, year));
                            System.out.println("Book added");
                        } else {
                            System.out.println("Branch missing");
                        }
                        break;

                    case 9: // Add Patron
                        System.out.print("Enter Patron ID: ");
                        String id = sc.nextLine();
                        System.out.print("Enter Patron Name: ");
                        String name = sc.nextLine();
                        lib.addPatron(id, name);
                        System.out.println("Patron added");
                        break;

                    case 10: // List Branches
                        System.out.println("Existing branches:");
                        lib.getAllBranches().forEach(b -> System.out.println("- " + b.getBranchId()));
                        break;

                    case 0:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception ex) {
                LOG.severe(() -> "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        } while (choice != 0);

        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Library Menu ===");
        System.out.println("1. List all books");
        System.out.println("2. Search book by title");
        System.out.println("3. Search book by author");
        System.out.println("4. Checkout book");
        System.out.println("5. Return book");
        System.out.println("6. Reserve book");
        System.out.println("7. Recommend books");
        System.out.println("8. Add book");
        System.out.println("9. Add patron");
        System.out.println("10. list branches");
        System.out.println("0. Exit");
        System.out.println("====================");
    }
}
