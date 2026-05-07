package models;

public class Book {

    private int bookId;
    private String title;

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public Book(int bookId, String title) {
        this.bookId = bookId;
        this.title = title;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "bookId=" + bookId
                + ", title=" + title + "\n";
    }
}