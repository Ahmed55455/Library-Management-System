package librarymanagementsystem;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    // MEMORY DATABASE (csv)
    private List<User> users = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Problem> problems = new ArrayList<>();

    // FILE NAMES 
    private final String FILE_USERS = "users.csv";
    private final String FILE_BOOKS = "books.csv";
    private final String FILE_LOANS = "loans.csv";
    private final String FILE_MSGS = "messages.csv";
    private final String FILE_PROBS = "problems.csv";

    public LibraryManager() {
        loadAllData(); // Load everything when the app starts
    }


    // (Login & Register)
    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null; // Login failed
    }

    //  Register Student (Prevents Duplicates)
    public boolean registerStudent(String username, String password, String name, String surname, String email, String studentId, int age) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return false; // Fail: Username is taken!
            }
        }
        Student newStudent = new Student(username, password, name, surname, email, studentId, age);
        users.add(newStudent);
        saveAllData(); 
        return true;
    }

    //  Register Admin (Prevents Duplicates)
    public void registerAdmin(String username, String password, String name, String surname, String email) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("⚠️ Warning: Admin username '" + username + "' already exists. Skipping.");
                return; 
            }
        }
        Admin newAdmin = new Admin(username, password, name, surname, email);
        users.add(newAdmin);
        saveAllData();
    }
    
    public boolean deleteUser(String username) {
        boolean removed = users.removeIf(u -> u.getUsername().equals(username));
        if (removed) saveAllData();
        return removed;
    }
    
    public boolean changePassword(String username, String newPass) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                u.setPassword(newPass);
                saveAllData();
                return true;
            }
        }
        return false;
    }
    // 2. BOOK MANAGEMENT
    public void addBook(Book b) {
        books.add(b);
        saveAllData();
    }

    public boolean removeBook(String isbn) {
        boolean removed = books.removeIf(b -> b.getIsbn().equals(isbn));
        saveAllData();
        return removed;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.search(keyword)) results.add(b);
        }
        return results;
    }

    public Book findBook(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) return b;
        }
        return null;
    }
    // 3. LOAN MANAGEMENT
    public boolean borrowBook(String isbn, User user) {
        if (!(user instanceof Student)) return false; 
        Student s = (Student) user;
        Book b = findBook(isbn);

        if (b != null && b.isAvailable()) {
            b.setAvailable(false);
            
            // ROBUST ID GENERATION
            int newId = 1;
            if (!loans.isEmpty()) {
                // Get the ID of the last loan and add 1
                newId = loans.get(loans.size() - 1).getId() + 1;
            }
        
            loans.add(new Loan(newId, b, s, LocalDate.now(), null));
            saveAllData();
            return true;
        }
        return false;
    }

    public double returnBook(String isbn, User user) {
        for (Loan l : loans) {
            if (l.getBook().getIsbn().equals(isbn) && 
                l.getStudent().getUsername().equals(user.getUsername()) && 
                l.getReturnDate() == null) {
                
                l.returnBook(LocalDate.now());
                saveAllData();
                
                // Calculate Fee (Assume 7 day limit)
                int daysLate = Math.max(0, l.getDaysKept() - 7);
                return l.getStudent().calculateFee(daysLate);
            }
        }
        return -1; // Loan not found
    }

    public List<Loan> getStudentHistory(String username) {
        List<Loan> history = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getStudent().getUsername().equals(username)) {
                history.add(l);
            }
        }
        return history;
    }
    
    public List<Loan> getAllLoans() { return loans; } 
  
    // MESSAGING & PROBLEMS
    public boolean sendMessage(String sender, String receiver, String content) {
        boolean found = false;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(receiver) && u instanceof Student) {
                found = true;
                break;
            }
        }
        
        if (!found) return false; 

        // -ROBUST ID GENERATION 
        int id = 1;
        if (!messages.isEmpty()) {
            id = messages.get(messages.size() - 1).getId() + 1;
        }
        
        messages.add(new Message(id, sender, receiver, content, LocalDate.now()));
        saveAllData();
        return true;
    }

    public void deleteMessage(int id) {
        messages.removeIf(m -> m.getId() == id);
        saveAllData();
    }

    public void reportProblem(String username, String desc) {
        //  ROBUST ID GENERATION
        int id = 1;
        if (!problems.isEmpty()) {
            id = problems.get(problems.size() - 1).getId() + 1;
        }
        

        problems.add(new Problem(id, username, desc, LocalDate.now()));
        saveAllData();
    }
    
    public List<Problem> getAllProblems() { return problems; }
    
    public void resolveProblem(int id) {
        problems.removeIf(p -> p.getId() == id);
        saveAllData();
    }
    //  (CSV HANDLING)
    private void saveAllData() {
        saveList(FILE_USERS, users);
        saveList(FILE_BOOKS, books);
        saveList(FILE_LOANS, loans);
        saveList(FILE_MSGS, messages);
        saveList(FILE_PROBS, problems);
    }
    
    private void saveList(String filename, List<?> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Object obj : list) {
                if (obj instanceof User) bw.write(((User)obj).toCSV());
                else if (obj instanceof Book) bw.write(((Book)obj).toCSV());
                else if (obj instanceof Loan) bw.write(((Loan)obj).toCSV());
                else if (obj instanceof Message) bw.write(((Message)obj).toCSV());
                else if (obj instanceof Problem) bw.write(((Problem)obj).toCSV());
                
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving " + filename);
        }
    }

    private void loadAllData() {
        loadUsers();
        loadBooks();
        loadLoans(); // Must load users/books first!
        loadMessages();
        loadProblems();
    }
    
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_USERS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 6) continue;
                // username,password,role,first,last,email,[studentId,age]
                if (p[2].equals("Student")) {
                    users.add(new Student(p[0], p[1], p[3], p[4], p[5], p[6], Integer.parseInt(p[7])));
                } else {
                    users.add(new Admin(p[0], p[1], p[3], p[4], p[5]));
                }
            }
        } catch (Exception e) {} 
    }
    
    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_BOOKS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                // ISBN,Title,Author,Category,Available
                if (p.length >= 5) {
                    books.add(new Book(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[4])));
                }
            }
        } catch (Exception e) {}
    }
    
    private void loadLoans() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_LOANS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                // ID,BookISBN,StudentID,LoanDate,ReturnDate
                if (p.length >= 5) {
                    Book b = findBook(p[1]);
                    // Find student inside Users list
                    Student s = null;
                    for(User u : users) {
                        if(u instanceof Student && ((Student)u).getStudentId().equals(p[2])) {
                            s = (Student)u; break;
                        }
                    }
                    
                    if (b != null && s != null) {
                        LocalDate rDate = p[4].equals("null") ? null : LocalDate.parse(p[4]);
                        loans.add(new Loan(Integer.parseInt(p[0]), b, s, LocalDate.parse(p[3]), rDate));
                    }
                }
            }
        } catch (Exception e) {}
    }
    
    private void loadMessages() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_MSGS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                messages.add(new Message(Integer.parseInt(p[0]), p[1], p[2], p[3], LocalDate.parse(p[4])));
            }
        } catch (Exception e) {}
    }
    
    private void loadProblems() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PROBS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                problems.add(new Problem(Integer.parseInt(p[0]), p[1], p[2], LocalDate.parse(p[3])));
            }
        } catch (Exception e) {}
    }

    public void changePassword(User user, String newPass) {
        user.setPassword(newPass); 
        saveAllData(); 
    }

   public boolean deleteAccount(User user) {
        if (user instanceof Student) {
            for (Loan l : loans) {
                if (l.getStudent().getUsername().equals(user.getUsername()) && l.getReturnDate() == null) {
                    return false;
                }
            }
        }
        users.remove(user);
        saveAllData();
        return true;
    }

    public List<Message> getMyMessages(String username) {
        List<Message> myMsgs = new ArrayList<>();
        for (Message m : messages) {
            if (m.getReceiver().equalsIgnoreCase(username)) myMsgs.add(m);
        }
        return myMsgs;
    }
}
