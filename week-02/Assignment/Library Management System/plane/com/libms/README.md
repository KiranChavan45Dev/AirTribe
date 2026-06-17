# 📚 Library Management System (Java, Console-Based)

A console-based **Library Management System (LMS)** built in **Java 11** for demonstration of **OOP concepts, SOLID principles, and design patterns**.  
The system manages **books, patrons, branches, and lending operations** without requiring a database.  

---

## ✨ Features

### Core
- **Book Management**
  - Add, remove, and search books (by title, author, ISBN).
  - Track borrowed and available books.
- **Patron Management**
  - Add and update patrons.
  - Track borrowing history.
- **Lending Process**
  - Book checkout and return.
  - Reservation system for unavailable books.
- **Inventory Management**
  - Branch-specific inventory handling.
  - Prevents duplicate checkouts.

### Extensions
- **Multi-Branch Support**
  - Create multiple branches.
  - View all branches.
- **Reservation**
  - Patrons can reserve books.
  - Notification placeholder for availability.
- **Recommendations**
  - Suggests books based on patron borrowing history (basic strategy).
- **Design Patterns**
  - **Factory Pattern** → for creating `Book` objects.
  - **Strategy / Observer (extendable)** → for recommendation and notification system.

---

## 🛠️ Technical Stack
- **Language**: Java 11  
- **IDE**: VS Code (or any Java-compatible IDE)  
- **Build Tool**: None (plain Java, `javac` and `java`)  
- **Logging**: `java.util.logging.Logger`  
- **Collections**: `List`, `Set`, `Map`  

---

## 📂 Project Structure

+--- com
|   +--- libms
|   |   +--- core
|   |   |   +--- Branch.java
|   |   |   +--- Inventory.java
|   |   |   +--- Library.java
|   |   |   +--- Reservation.java
|   |   +--- factory
|   |   |   +--- BookFactory.java
|   |   |   +--- PatronFactory.java
|   |   +--- Main.java
|   |   +--- model
|   |   |   +--- Book.java
|   |   |   +--- Patron.java
|   |   +--- notify
|   |   |   +--- NotificationService.java
|   |   +--- README.md
|   |   +--- recommend
|   |   |   +--- RecommendationService.java

---

## 🏗️ Design Principles & Patterns

- **OOP Concepts**
  - `Book`, `Patron`, `Branch`, `Inventory` → Encapsulation & Abstraction
  - `BookFactory`, `PatronFactory` → Polymorphism via Factory
- **SOLID**
  - **S**ingle Responsibility → Each class handles a single concern
  - **O**pen/Closed → Easily extendable for new features
  - **L**iskov Substitution → Interfaces (e.g., recommend strategies) interchangeable
  - **I**nterface Segregation → Lightweight services
  - **D**ependency Inversion → High-level modules don’t depend on low-level details
- **Design Patterns**
  - **Factory** → `BookFactory`, `PatronFactory`
  - **Observer (Publisher-Subscriber)** → `NotificationService` for reservations
  - **Strategy** → `RecommendationService` for recommendations

---

## 🖥️ How to Run

### Requirements
- **Java JDK 11**
- **VS Code** or any Java IDE


### Steps
1. Clone or download the project
    ```bash
    git clone https://github.com/yourusername/library-management-system.git
    cd library-management-system

2. Compile the project
    ```bash
    javac com/libms/**/*.java

3. Run the program
    ```bash
    java com.libms.Main

---

# 📊 Example Console Flow

=== Library Menu ===
1. List all books
2. Search book by title
3. Search book by author
4. Checkout book
5. Return book
6. Reserve book
7. Recommend books
8. Add book
9. Add patron
10. list branches
0. Exit
====================


# Logging

## The project uses java.util.logging for:

- Successful operations (adding books, patrons, etc.)

- Warnings (duplicate ISBNs, unavailable books)

- Errors (invalid inputs)