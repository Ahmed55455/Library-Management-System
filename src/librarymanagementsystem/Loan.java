package librarymanagementsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private int id; // Unique ID for the loan (useful for deleting)
    private Book book;
    private Student student;
    private LocalDate loanDate;
    private LocalDate returnDate; // Null if not returned yet

    public Loan(int id, Book book, Student student, LocalDate loanDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.student = student;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public int getId() { return id; }
    public Book getBook() { return book; }
    public Student getStudent() { return student; }
    public LocalDate getReturnDate() { return returnDate; }

    public void returnBook(LocalDate date) {
        this.returnDate = date;
        this.book.setAvailable(true); // Make the book free again
    }

    // Calculate how many days the book was kept
    public int getDaysKept() {
        if (returnDate == null) return 0; 
        return (int) ChronoUnit.DAYS.between(loanDate, returnDate);
    }

    // ID,BookISBN,StudentID,LoanDate,ReturnDate this is the format for the CSV file
    public String toCSV() {
        String rDate = (returnDate == null) ? "null" : returnDate.toString();
        return id + "," + book.getIsbn() + "," + student.getStudentId() + "," + loanDate + "," + rDate;
    }

    @Override
    public String toString() {
        String status = (returnDate == null) ? "Active" : "Returned on " + returnDate;
        return "[Loan ID: " + id + "] " + book.getTitle() + " -> " + student.getUsername() + " (" + status + ")";
    }

    Object getStudentUsername() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
