package librarymanagementsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {

    private Scanner scanner = new Scanner(System.in);
    private LibraryManager manager = new LibraryManager();
    private User currentUser;

    public void start() {
        // Setup default admin
        manager.registerAdmin("admin", "admin123", "Super", "Admin", "admin@library.com");

        while (true) {
            System.out.println("\n=================================");
            System.out.println("    WELCOME TO LIBRARY SYSTEM    ");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Register New Account");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    performLogin();
                    break;
                case "2":
                    showRegistrationMenu();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void performLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        currentUser = manager.login(user, pass);

        if (currentUser != null) {
            System.out.println("Login Successful! Welcome, " + currentUser.getUsername());
            if (currentUser instanceof Admin) {
                adminMenu();
            } else {
                studentMenu();
            }
        } else {
            System.out.println("Invalid password.");
        }
    }

    private void showRegistrationMenu() {
        System.out.println("\n--- REGISTER NEW ACCOUNT ---");
        System.out.println("1. Register as STUDENT");
        System.out.println("2. Register as ADMIN");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                registerStudentFlow();
                break;
            case "2":
                registerAdminFlow();
                break;
            case "3":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void studentMenu() {
        while (true) {
            System.out.println("\n--- STUDENT DASHBOARD ---");
            System.out.println("1. Borrow Book (Search by Title)");
            System.out.println("2. Return Book (Select from List)");
            System.out.println("3. Search for a Book");
            System.out.println("4. View My Inbox");
            System.out.println("5. Report a Problem");
            System.out.println("6. My Loan History");
            System.out.println("7. Settings"); 
            System.out.println("8. Logout"); 
            System.out.print("Choice: ");
            
            int choice = -1;
            try { choice = Integer.parseInt(scanner.nextLine()); } catch(Exception e) {}

            switch (choice) {
                case 1: // NEW BORROW LOGIC (Search & Select) 
                    System.out.print("Enter part of Book Title: ");
                    String keyword = scanner.nextLine();
                    List<Book> results = manager.searchBooks(keyword);
                    
                    if (results.isEmpty()) {
                        System.out.println("No books found matching '" + keyword + "'");
                    } else {
                        System.out.println("--- Select a book to borrow ---");
                        for (int i = 0; i < results.size(); i++) {
                            System.out.println((i + 1) + ". " + results.get(i));
                        }
                        System.out.print("Enter number (0 to cancel): ");
                        try {
                            int selection = Integer.parseInt(scanner.nextLine());
                            if (selection > 0 && selection <= results.size()) {
                                Book selectedBook = results.get(selection - 1);
                                if (manager.borrowBook(selectedBook.getIsbn(), currentUser)) {
                                    System.out.println("Success! You borrowed: " + selectedBook.getTitle());
                                } else {
                                    System.out.println("Failed. The book might be unavailable.");
                                }
                            }
                        } catch(Exception e) { System.out.println("Invalid selection."); }
                    }
                    break;

                case 2: // NEW RETURN LOGIC (Select from My Loans) 
                    //  Get all history
                    List<Loan> history = manager.getStudentHistory(currentUser.getUsername());
                    //  Filter for only ACTIVE loans (where returnDate is null)
                    List<Loan> activeLoans = new ArrayList<>();
                    for (Loan l : history) {
                        if (l.getReturnDate() == null) {
                            activeLoans.add(l);
                        }
                    }

                    if (activeLoans.isEmpty()) {
                        System.out.println(" You have no books to return.");
                    } else {
                        System.out.println("--- Select a book to return ---");
                        for (int i = 0; i < activeLoans.size(); i++) {
                            // Show index + Book Title
                            System.out.println((i + 1) + ". " + activeLoans.get(i).getBook().getTitle());
                        }
                        System.out.print("Enter number (0 to cancel): ");
                        try {
                            int selection = Integer.parseInt(scanner.nextLine());
                            if (selection > 0 && selection <= activeLoans.size()) {
                                Loan loanToReturn = activeLoans.get(selection - 1);
                                double fee = manager.returnBook(loanToReturn.getBook().getIsbn(), currentUser);
                                
                                if (fee != -1) {
                                    System.out.println(" Returned: " + loanToReturn.getBook().getTitle());
                                    if (fee > 0) System.out.println("⚠️ LATE FEE APPLIED: $" + fee);
                                }
                            }
                        } catch(Exception e) { System.out.println("Invalid selection."); }
                    }
                    break;

                case 3:
                    System.out.print("Enter search keyword: ");
                    List<Book> searchRes = manager.searchBooks(scanner.nextLine());
                    if (searchRes.isEmpty()) System.out.println("No books found.");
                    else for (Book b : searchRes) System.out.println(b);
                    break;
                case 4:
                    List<Message> msgs = manager.getMyMessages(currentUser.getUsername());
                    if (msgs.isEmpty()) System.out.println("No messages.");
                    else for (Message m : msgs) System.out.println(m);
                    break;
                case 5:
                    System.out.print("Describe issue: ");
                    manager.reportProblem(currentUser.getUsername(), scanner.nextLine());
                    System.out.println(" Problem reported.");
                    break;
                case 6:
                    List<Loan> fullHistory = manager.getStudentHistory(currentUser.getUsername());
                    for (Loan l : fullHistory) System.out.println(l);
                    break;
                case 7: 
                    settingsMenu();
                    if (currentUser == null) return; 
                    break;
                case 8: 
                    return; 
            }
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            currentUser.showMenu(); 
            System.out.print("Choice: ");
            
            int choice = -1;
            try { choice = Integer.parseInt(scanner.nextLine()); } catch(Exception e) {}

            switch (choice) {
                case 1:
                    System.out.print("ISBN: "); String isbn = scanner.nextLine();
                    System.out.print("Title: "); String title = scanner.nextLine();
                    System.out.print("Author: "); String auth = scanner.nextLine();
                    System.out.print("Category: "); String cat = scanner.nextLine();
                    manager.addBook(new Book(isbn, title, auth, cat, true));
                    System.out.println(" Book Added.");
                    break;
                case 2:
                    System.out.print("ISBN to remove: ");
                    if (manager.removeBook(scanner.nextLine())) System.out.println(" Removed.");
                    else System.out.println(" Not found.");
                    break;
                case 3:
                    for (Loan l : manager.getAllLoans()) System.out.println(l);
                    break;
                case 4: 
                    System.out.print("Student Username: "); 
                    String receiver = scanner.nextLine();
                    System.out.print("Message: "); 
                    String content = scanner.nextLine();
                    
                    if (manager.sendMessage("Admin", receiver, content)) {
                        System.out.println(" Sent successfully.");
                    } else {
                        System.out.println(" There is no student called " + receiver);
                    }
                    break;
                case 5:
                    List<Problem> probs = manager.getAllProblems();
                    if (probs.isEmpty()) System.out.println("No problems reported.");
                    else {
                        for (Problem p : probs) System.out.println(p);
                        System.out.print("Enter Problem ID to resolve (0 to cancel): ");
                        try {
                            int pid = Integer.parseInt(scanner.nextLine());
                            if (pid != 0) manager.resolveProblem(pid);
                        } catch(Exception e) {}
                    }
                    break;                    
                case 7: 
                    settingsMenu();
                    if (currentUser == null) return; 
                    break;
                case 6:
                    return;
            }
        }
    }

    private void registerStudentFlow() {
        System.out.println("--- NEW STUDENT REGISTRATION ---");
        System.out.print("Username: "); String user = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        System.out.print("First Name: "); String first = scanner.nextLine();
        System.out.print("Last Name: "); String last = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Student ID: "); String sid = scanner.nextLine();
        System.out.print("Age: "); 
        try {
            int age = Integer.parseInt(scanner.nextLine());
            if (manager.registerStudent(user, pass, first, last, email, sid, age)) {
                System.out.println(" Registered! Please login now.");
            } else {
                System.out.println(" Username already exists.");
            }
        } catch (Exception e) {
            System.out.println("Invalid age.");
        }
    }

    private void registerAdminFlow() {
        System.out.println("\n--- NEW ADMIN REGISTRATION ---");
        System.out.print(" ENTER SECURITY CODE: ");
        String inputCode = scanner.nextLine();
        
        String realCode = "";
        try {
            java.io.File file = new java.io.File("admin_secret.txt");
            java.util.Scanner fileScanner = new java.util.Scanner(file);
            if (fileScanner.hasNextLine()) {
                realCode = fileScanner.nextLine().trim(); 
            }
            fileScanner.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println(" Error: 'admin_secret.txt' file is missing!");
            return;
        }

        if (!inputCode.equals(realCode)) {
            System.out.println(" ACCESS DENIED! Wrong Security Code.");
            return;
        }
        
        System.out.println(" Code Accepted. Proceeding...");
        System.out.print("Username: "); String user = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        System.out.print("First Name: "); String first = scanner.nextLine();
        System.out.print("Last Name: "); String last = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        
        manager.registerAdmin(user, pass, first, last, email);
        System.out.println(" New Admin Registered Successfully!");
    }

    private void settingsMenu() {
        while (true) {
            System.out.println("\n--- SETTINGS ---");
            System.out.println("1. Change Password");
            System.out.println("2. Delete My Account");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choice: ");

            int choice = -1;
            try { choice = Integer.parseInt(scanner.nextLine()); } catch(Exception e) {}

            switch (choice) {
                case 1: 
                    System.out.print("Enter New Password: ");
                    String newPass = scanner.nextLine();
                    manager.changePassword(currentUser, newPass);
                    System.out.println(" Password Updated!");
                    break;

                case 2: 
                    System.out.print("Are you sure? This cannot be undone! (type 'yes'): ");
                    if (scanner.nextLine().equalsIgnoreCase("yes")) {
                        if (manager.deleteAccount(currentUser)) {
                            System.out.println(" Account Deleted. Goodbye.");
                            currentUser = null; 
                            return; 
                        } else {
                            System.out.println(" Cannot delete account! You still have borrowed books.");
                        }
                    }
                    break;

                case 3: 
                    return;
            }
            
            if (currentUser == null) return;
        }
    }
}
