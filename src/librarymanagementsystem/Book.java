package librarymanagementsystem;

public class Book implements Searchable {
    private String isbn;
    private String title;
    private String author;
    private String category; // Added Category 
    private boolean isAvailable;

    public Book(String isbn, String title, String author, String category, boolean isAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean search(String keyword) {
        String k = keyword.toLowerCase();
        return title.toLowerCase().contains(k) || 
               author.toLowerCase().contains(k) || 
               category.toLowerCase().contains(k) ||
               isbn.contains(k);
    }

    // CSV : ISBN,Title,Author,Category,Available
    public String toCSV() {
        return isbn + "," + title + "," + author + "," + category + "," + isAvailable;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "[Available]" : "[Borrowed]";
        return status + " " + title + " (" + category + ")";
    }
}
