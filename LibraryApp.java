import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Book implements Serializable {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

class Bookshelf implements Serializable {
    private transient List<Book> books; 

    public Bookshelf() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); 
        out.writeInt(books.size()); 
        for (Book book : books) {
            out.writeObject(book.getTitle());
            out.writeObject(book.getAuthor());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); 
        int size = in.readInt(); 
        books = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String title = (String) in.readObject();
            String author = (String) in.readObject();
            Book book = new Book(title, author);
            books.add(book);
        }
    }

    @Override
    public String toString() {
        return "Bookshelf{" +
                "books=" + books +
                '}';
    }
}

public class LibraryApp {
    public static void main(String[] args) {
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        Bookshelf bookshelf = new Bookshelf();
        bookshelf.addBook(book1);
        bookshelf.addBook(book2);
        bookshelf.addBook(book3);

        saveBookshelf(bookshelf, "bookshelf.ser");

        Bookshelf restoredBookshelf = loadBookshelf("bookshelf.ser");
        System.out.println("Restored Bookshelf:");
        System.out.println(restoredBookshelf);
    }

    private static void saveBookshelf(Bookshelf bookshelf, String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(bookshelf);
            System.out.println("Bookshelf saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Bookshelf loadBookshelf(String filename) {
        Bookshelf bookshelf = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            bookshelf = (Bookshelf) inputStream.readObject();
            System.out.println("Bookshelf loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bookshelf;
    }
}