package librarymanagementsystem;

public abstract class User implements Searchable {
    private String username;
    private String password; 
    private String role;     // "Student" or "Admin"
    private String firstName;
    private String lastName;
    private String email;

    public User(String username, String password, String role, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }

    public void setPassword(String newPassword) { this.password = newPassword; }

    // Abstract method (Polymorphism)
    public abstract void showMenu(); 

    @Override
    public boolean search(String keyword) {
        return username.equalsIgnoreCase(keyword) || firstName.equalsIgnoreCase(keyword);
    }
    
    //  username,password,role,firstname,lastname,email
    public String toCSV() {
        return username + "," + password + "," + role + "," + firstName + "," + lastName + "," + email;
    }
}
