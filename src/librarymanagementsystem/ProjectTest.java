package librarymanagementsystem;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.List;

public class ProjectTest {

    // 1. TEST STUDENT CLASS
    @Test
    public void testStudentClass() {
        System.out.println("Testing Student Class...");
        Student s = new Student("s_user", "123", "John", "Doe", "j@d.com", "S101", 20);

        assertEquals("Student", s.getRole());
        assertEquals("S101", s.getStudentId());
        
        // Logic: Students pay $0.50 per late day
        assertEquals(2.50, s.calculateFee(5), 0.01);
        
        // Search: Should find by username
        assertTrue(s.search("s_user"));
        
        // CSV Format
        String csv = s.toCSV();
        assertTrue(csv.contains("s_user") && csv.contains("S101"));
        
        System.out.println("✔ Student Class Passed");
    }


    // 2. TEST ADMIN CLASS
  
    @Test
    public void testAdminClass() {
        System.out.println("Testing Admin Class...");
        Admin a = new Admin("admin_user", "pass", "Super", "Admin", "a@a.com");

        assertEquals("Admin", a.getRole());
        
        // Admin CSV should end with "N/A,0" (placeholder for ID and age)
        String csv = a.toCSV();
        assertTrue(csv.contains("N/A,0"));
        
        System.out.println("✔ Admin Class Passed");
    }

    // 3. TEST BOOK CLASS

    @Test
    public void testBookClass() {
        System.out.println("Testing Book Class...");
        Book b = new Book("999-1", "Java Programming", "Gosling", "Tech", true);

        // Test Getters
        assertEquals("Java Programming", b.getTitle());
        assertTrue(b.isAvailable());

        // Test Search Logic
        assertTrue(b.search("Java"));    // Title
        assertTrue(b.search("Gosling")); // Author
        assertFalse(b.search("Python")); // Invalid

        // Test Availability Toggle
        b.setAvailable(false);
        assertFalse(b.isAvailable());
        
        // CSV Format
        assertTrue(b.toCSV().contains("999-1"));
        
        System.out.println("✔ Book Class Passed");
    }

    // 4. TEST LOAN CLASS
    @Test
    public void testLoanClass() {
        System.out.println("Testing Loan Class...");
        Book b = new Book("B1", "Book1", "Auth", "Cat", false);
        Student s = new Student("s1", "p", "N", "S", "e", "ID", 20);
        
        // Create a loan starting 10 days ago
        LocalDate loanDate = LocalDate.now().minusDays(10);
        Loan loan = new Loan(1, b, s, loanDate, null);

        assertNull(loan.getReturnDate());
        
        // Return the book today
        loan.returnBook(LocalDate.now());
        
        assertNotNull(loan.getReturnDate());
        assertEquals(10, loan.getDaysKept()); // Should be exactly 10 days
        assertTrue(b.isAvailable()); // Book should be free now
        
        System.out.println("✔ Loan Class Passed");
    }
    // 5. TEST MESSAGE CLASS

    @Test
    public void testMessageClass() {
        System.out.println("Testing Message Class...");
        Message msg = new Message(100, "Admin", "Student1", "Hello World", LocalDate.now());
        
        assertEquals(100, msg.getId());
        assertEquals("Student1", msg.getReceiver());
        assertTrue(msg.toString().contains("Hello World"));
        
        System.out.println("✔ Message Class Passed");
    }

    // 6. TEST PROBLEM CLASS
    @Test
    public void testProblemClass() {
        System.out.println("Testing Problem Class...");
        Problem p = new Problem(50, "Student1", "Torn page", LocalDate.now());
        
        assertEquals(50, p.getId());
        assertEquals("Student1", p.getUsername());
        assertTrue(p.toCSV().contains("Torn page"));
        
        System.out.println("✔ Problem Class Passed");
    }

    // 7. INTEGRATION TEST (Register -> Borrow -> Return)
    @Test
    public void testFullSystemIntegration() {
        System.out.println("Testing Full System Integration...");
        LibraryManager manager = new LibraryManager();

        // 1. Clean up old data to ensure clean test
        manager.deleteUser("test_integration_student");
        manager.removeBook("INT-BOOK-1");

        // 2. Register
        boolean reg = manager.registerStudent("test_integration_student", "pass", "Test", "User", "t@t.com", "T1", 22);
        assertTrue("Registration should succeed", reg);

        // 3. Login
        User u = manager.login("test_integration_student", "pass");
        assertNotNull("Login should return user", u);

        // 4. Add Book
        manager.addBook(new Book("INT-BOOK-1", "Integration Test Book", "Tester", "Code", true));

        // 5. Borrow
        boolean borrowed = manager.borrowBook("INT-BOOK-1", u);
        assertTrue("Borrowing should succeed", borrowed);
        
        // 6. Verify Borrow (Book is unavailable)
        Book b = manager.findBook("INT-BOOK-1");
        assertFalse(b.isAvailable());

        // 7. Return
        double fee = manager.returnBook("INT-BOOK-1", u);
        assertTrue("Fee calculation should run", fee >= 0);
        assertTrue("Book should be available again", b.isAvailable());

        // Cleanup
        manager.deleteUser("test_integration_student");
        manager.removeBook("INT-BOOK-1");
        
        System.out.println("✔ System Integration Passed");
    }

    // 8. TEST ALL REMAINING BUTTONS (Admin & Student)
    @Test
    public void testAllButtonActions() {
        System.out.println("Testing All Menu Buttons...");
        LibraryManager manager = new LibraryManager();
        
        // --- PREPARE DATA ---
        // Clean up from previous runs just in case
        manager.deleteUser("btnStudent");
        manager.deleteUser("btnAdmin");
        manager.removeBook("RM-101");
        
        manager.registerStudent("btnStudent", "pass", "S", "T", "e@e.com", "B1", 20);
        manager.registerAdmin("btnAdmin", "pass", "A", "D", "a@a.com");
        User student = manager.login("btnStudent", "pass");
        User admin = manager.login("btnAdmin", "pass");
        
        // TEST ADMIN BUTTON 4: Send Message
        boolean sent = manager.sendMessage("btnAdmin", "btnStudent", "Welcome!");
        assertTrue("Admin should be able to send message", sent);
        
        // TEST STUDENT BUTTON 4: View Inbox
      
        List<Message> inbox = manager.getMyMessages("btnStudent");
        assertFalse("Student inbox should not be empty", inbox.isEmpty());
        // Check if the latest message is "Welcome!"
        String lastMsg = inbox.get(inbox.size() - 1).toString();
        assertTrue(lastMsg.contains("Welcome!"));

        // TEST STUDENT BUTTON 5: Report Problem

        manager.reportProblem("btnStudent", "Noise in library");
        List<Problem> probs = manager.getAllProblems();
        assertFalse("Problems list should not be empty", probs.isEmpty());
        
        // TEST ADMIN BUTTON 5: Resolve Problem
      
        int probId = probs.get(probs.size()-1).getId();
        manager.resolveProblem(probId);
        
        // Check if it's gone
        boolean found = false;
        for(Problem p : manager.getAllProblems()) {
            if (p.getId() == probId) found = true;
        }
        assertFalse("Problem should be deleted after resolve", found);

        // TEST STUDENT BUTTON 7: Change Password
        manager.changePassword(student, "newPass123");
        User relogin = manager.login("btnStudent", "newPass123");
        assertNotNull("Should login with new password", relogin);
        
        // TEST ADMIN BUTTON 2: Remove Book
        manager.addBook(new Book("RM-101", "Remove Me", "A", "C", true));
        assertTrue(manager.removeBook("RM-101"));
        assertNull(manager.findBook("RM-101"));

        // TEST STUDENT BUTTON 7: Delete Account
        boolean deleted = manager.deleteAccount(student);
        assertTrue("Account deletion should succeed", deleted);
        assertNull("User should not be able to login anymore", manager.login("btnStudent", "newPass123"));
        
        // Cleanup Admin
        manager.deleteUser("btnAdmin");
        
        System.out.println("✔ ALL BUTTONS CHECKED & PASSED");
    }
}
