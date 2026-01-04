package librarymanagementsystem;

import java.time.LocalDate;

public class Problem {
    private int id;
    private String username;
    private String description;
    private LocalDate date;

   public Problem(int id, String username, String description, LocalDate date) {
    this.id = id;
    this.username = username;
    this.description = description;
    this.date = date;
}

    Problem(int i, String user, String desc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public int getId() { return id; }
    public String getUsername() { return username; }

    public String toCSV() {
        return id + "," + username + "," + description + "," + date;
    }
    
    @Override
    public String toString() {
        return "[ID: " + id + "] User: " + username + " reported: " + description;
    }
}
