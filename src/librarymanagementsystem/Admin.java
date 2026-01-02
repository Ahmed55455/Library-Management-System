package librarymanagementsystem;

public class Admin extends User {

    public Admin(String username, String password, String firstName, String lastName, String email) {
        super(username, password, "Admin", firstName, lastName, email);
    }

    @Override
    public String toCSV() {
        // Admins don't have student IDs, so we put "N/A,0" to keep CSV structure consistent
        return super.toCSV() + ",N/A,0";
    }

    @Override
    public void showMenu() {
        System.out.println("1. Add New Book");
        System.out.println("2. Remove Book");
        System.out.println("3. View All Loans");
        System.out.println("4. Send Message to Student");
        System.out.println("5. View/Resolve Problems");
        System.out.println("6. Logout");
    }
}
