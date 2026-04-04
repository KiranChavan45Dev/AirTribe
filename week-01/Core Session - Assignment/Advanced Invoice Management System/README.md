# 📄 Advanced Invoice Management System

A **Java-based console application** designed to manage customers, products, invoices, and analytics for a small business.

---

## 🚀 Features

### 👤 Customer Management

* Add / Update customers
* Customer types:

    * Regular
    * Premium (discount + loyalty points)
    * Corporate (credit limit, tax exemption)
* Search by:

    * ID
    * Name
    * Email
* Purchase history tracking
* Lifetime value calculation

---

### 📦 Product Management

* Product types:

    * Physical Products
    * Digital Products
* Categories:

    * Electronics, Clothing, Books, Food
* Features:

    * Add / Update products
    * Stock tracking
    * Low stock alerts
    * Customer-specific pricing

---

### 🧾 Invoice Management

* Create invoices with multiple items
* Automatic:

    * Tax calculation (GST, VAT, Service Tax)
    * Discount calculation
* Payment handling:

    * Partial payments
    * Payment tracking
* Unique invoice IDs

---

### 📊 Analytics (Java Streams)

* Top customers
* Revenue insights
* Sales reports:

    * Daily / Monthly / Yearly
* Product performance
* Category-wise revenue

---

### 💾 File Operations

* Save & load:

    * Customers (`customers.dat`)
    * Products (`products.dat`)
    * Invoices (`invoices.dat`)
* Backup support
* Serialization-based persistence

---

## 🏗️ Project Structure

```
src/com/invoicesystem/
│
├── enums/              # All enums
├── model/              # Core entities
├── service/            # Business logic
├── Main.java           # Entry point
```

---

## ⚙️ How to Run

### 1. Compile

```bash
javac -d out src/com/invoicesystem/**/*.java
```

### 2. Run

```bash
java -cp out com.invoicesystem.Main
```

---

## 🖥️ Sample Console Menu

```
=== INVOICE MANAGEMENT SYSTEM ===

1. Customer Management
2. Product Management
3. Invoice Operations
4. Reports & Analytics
5. File Operations
0. Exit
```

---

## 🧪 Sample Runs (Terminal Style)

---

### ➤ Add Customer

```
Select Option: 1
1.1 Add Customer

Enter ID: C001
Enter Name: John Doe
Enter Email: john@gmail.com
Enter Phone: 9876543210
Enter Address: Mumbai
Select Type (1-Regular, 2-Premium, 3-Corporate): 2

Enter Discount %: 10
Enter Loyalty Points: 500

✅ Customer added successfully!
```

---

### ➤ Add Product

```
Select Option: 2
2.1 Add Product

Enter Product ID: P101
Enter Name: Laptop
Enter Category: ELECTRONICS
Enter Price: 50000
Enter Stock: 10
Enter Tax Type: GST

✅ Product added successfully!
```

---

### ➤ Create Invoice

```
Select Option: 3
3.1 Create Invoice

Enter Customer ID: C001

Invoice Created: INV-AB12CD34

Add Product:
Enter Product ID: P101
Enter Quantity: 2

✅ Product added to invoice
```

---

### ➤ View Invoice

```
Invoice: INV-AB12CD34
Customer: John Doe

Items:
Laptop x2 = 100000

Tax: 18000
Discount: 5000

Total: 113000
```

---

### ➤ Process Payment

```
Select Option: 3.4 Process Payment

Enter Invoice ID: INV-AB12CD34
Enter Amount: 113000
Select Payment Method: UPI

✅ Payment Successful
```

---

### ➤ Low Stock Alert

```
Select Option: 2.4 View Low Stock

⚠️ Low Stock Products:
- Laptop (Stock: 2)
```

---

### ➤ Analytics Example

```
Select Option: 4

Top Customers:
1. John Doe - ₹120000
2. Alice - ₹90000

Revenue by Category:
ELECTRONICS: ₹200000
FOOD: ₹50000
```

---

### ➤ Save Data

```
Select Option: 5.1 Export Data

Saving customers...
Saving products...
Saving invoices...

✅ Data saved successfully!
```

---

### ➤ Load Data

```
Select Option: 5.2 Import Data

Loading customers...
Loading products...
Loading invoices...

✅ Data loaded successfully!
```

---

## 🔒 Data Files Generated

```
customers.dat
products.dat
invoices.dat
```

---

## 🧠 Design Concepts Used

* OOP Principles:

    * Inheritance
    * Polymorphism
    * Encapsulation
* Java Features:

    * Streams API
    * Serialization
    * Enums
* Design Patterns:

    * Strategy (Tax handling - simplified)
    * Service Layer pattern

---

## 📌 Future Improvements

* Database integration (MySQL / PostgreSQL)
* REST API (Spring Boot)
* GUI (JavaFX / Web UI)
* Authentication & roles
* Advanced reporting dashboard

