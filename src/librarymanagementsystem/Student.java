package librarymanagementsystem;

public class Student extends User {
    private String studentId;
    private int age;

    public Student(String username, String password, String firstName, String lastName, String email, String studentId, int age) {
        super(username, password, "Student", firstName, lastName, email);
        this.studentId = studentId;
        this.age = age;
    }

    public String getStudentId() { return studentId; }

    // Required for late fees
    public double calculateFee(int daysLate) {
        return daysLate * 0.50$;
    }

    @Override
    public String toCSV() {
        // Add student specific fields
        return super.toCSV() + "," + studentId + "," + age;
    }

    @Override
    public void showMenu() {
       System.out.println("\n--- STUDENT DASHBOARD ---");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Search for a Book");
            System.out.println("4. View My Inbox");
            System.out.println("5. Report a Problem");
            System.out.println("6. My Loan History");
            System.out.println("7. settings Menu");
            System.out.println("8. Logout");
            System.out.print("Choice: ");
    }
}
