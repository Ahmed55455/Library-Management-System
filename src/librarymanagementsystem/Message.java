package librarymanagementsystem;

import java.time.LocalDate;

public class Message {
    private int id; 
    private String sender;
    private String receiver;
    private String content;
    private LocalDate date;

   public Message(int id, String sender, String receiver, String content, LocalDate date) {
    this.id = id;
    this.sender = sender;
    this.receiver = receiver;
    this.content = content;
    this.date = date;
}

    Message(String sender, String receiver, String content) {
        throw new UnsupportedOperationException("Not supported yet."); 

    public int getId() { return id; }
    public String getReceiver() { return receiver; }

    public String toCSV() {
        return id + "," + sender + "," + receiver + "," + content + "," + date;
    }

    @Override
    public String toString() {
        return "[ID: " + id + "] From " + sender + " (" + date + "): " + content;
    }
}
