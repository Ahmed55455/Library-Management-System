# 📚 Library Management System

A comprehensive, Java-based console application designed to manage library operations efficiently. This system handles user authentication, book inventory, loan tracking, and problem reporting using Object-Oriented Programming (OOP) principles and file-based persistence (CSV).

---

## 🚀 Features

### 👤 User Roles
* **Students:**
    * Search for books by Title or Author.
    * Borrow and Return books (with automatic late fee calculation).
    * View personal loan history.
    * Receive messages from Admins.
    * Report problems to the administration.
* **Admins:**
    * Add and Remove books from the inventory.
    * Register new Admins (secured via a secret code).
    * View all active loans.
    * Send messages to specific students.
    * Resolve reported problems.

### ⚙️ Core Functionality
* **Data Persistence:** All data (Users, Books, Loans, Messages) is saved automatically to CSV files, ensuring no data loss after closing the app.
* **Smart Search:** Find books quickly without needing exact ISBNs.
* **Security:** Password-protected accounts and a hidden security code for Admin registration.

---

## 🛠️ Installation & Setup

1.  **Open in IDE:**
    * Open the project folder in **NetBeans**, **IntelliJ**, or **Eclipse**.

2.  **Create Security File (Important!):**
    * To register an Admin account, the system checks for a security file.
    * Create a file named `admin_secret.txt` in the root project folder.
    * Type a secret code inside it (e.g., `9999`) and save.

3.  **Run the Application:**
    * Run the `Main.java` file located in `src/librarymanagementsystem/`.

---

## 📖 Usage Guide

### 1. Registration
* **Student:** Select "Register" -> "Student". Enter your details. Student ID and Age are required.
* **Admin:** Select "Register" -> "Admin". Enter the security code from `admin_secret.txt` to proceed.

### 2. Borrowing a Book (Student)
1.  Login as a Student.
2.  Select **"1. Borrow Book"**.
3.  Type part of a title (e.g., "Harry").
4.  Select the book number from the list to confirm.

### 3. Returning a Book (Student)
1.  Login as a Student.
2.  Select **"2. Return Book"**.
3.  The system displays a list of *your* currently borrowed books.
4.  Select the number to return. If late, a fee will be shown.

### 4. Managing Inventory (Admin)
1.  Login as an Admin.
2.  Select **"1. Add Book"** to add new items.
3.  Select **"2. Remove Book"** to delete items by ISBN.

---

## 📂 Project Structure

* `src/librarymanagementsystem/`
    * `Main.java` - Entry point of the application.
    * `LibraryManager.java` - Controller logic handling data and File I/O.
    * `User.java` (Abstract) - Parent class for users.
    * `Student.java` - Student specific logic (fees, limits).
    * `Admin.java` - Admin specific logic.
    * `Book.java` - Represents book objects.
    * `Loan.java` - Tracks borrowing transactions.
    * `ProjectTest.java` - JUnit test suite.

---

## 🧪 Testing

The project includes a comprehensive JUnit test suite `ProjectTest.java` that covers:
* ✅ All Class Logic (Student, Admin, Book, Loan)
* ✅ Critical Bug Fixes (ID Generation, Search Logic)
* ✅ Full System Workflows (Register -> Borrow -> Return)

To run tests, right-click `ProjectTest.java` and select **"Test File"**.
