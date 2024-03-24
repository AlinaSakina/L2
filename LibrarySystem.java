import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Book implements Externalizable {
    private String title;
    private String author;

    public Book() {}

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeObject(author);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        author = (String) in.readObject();
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author='" + author + "'}";
    }
}

class Bookshelf implements Externalizable {
    private List<Book> books;

    public Bookshelf() {
        books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(books);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        books = (List<Book>) in.readObject();
    }

    @Override
    public String toString() {
        return "Bookshelf{books=" + books + '}';
    }
}

class Reader implements Externalizable {
    private String name;

    public Reader() {}

    public Reader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "'}";
    }
}

class Rental implements Externalizable {
    private Map<Reader, Book> rentals;

    public Rental() {
        rentals = new HashMap<>();
    }

    public Map<Reader, Book> getRentals() {
        return rentals;
    }

    public void rentBook(Reader reader, Book book) {
        rentals.put(reader, book);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(rentals);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        rentals = (Map<Reader, Book>) in.readObject();
    }

    @Override
    public String toString() {
        return "Rental{rentals=" + rentals + '}';
    }
}

public class LibrarySystem implements Externalizable {
    private Bookshelf bookshelf;
    private Rental rental;

    public LibrarySystem() {
        bookshelf = new Bookshelf();
        rental = new Rental();
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public Rental getRental() {
        return rental;
    }

    public void rentBook(Reader reader, Book book) {
        rental.rentBook(reader, book);
        bookshelf.getBooks().remove(book); 
    }

    public void returnBook(Reader reader, Book book) {
        rental.getRentals().remove(reader, book);
        bookshelf.getBooks().add(book); 
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(bookshelf);
        out.writeObject(rental);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        bookshelf = (Bookshelf) in.readObject();
        rental = (Rental) in.readObject();
    }

    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();

        library.getBookshelf().addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        library.getBookshelf().addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
        library.getBookshelf().addBook(new Book("1984", "George Orwell"));

        Reader reader1 = new Reader("John Doe");

        library.rentBook(reader1, library.getBookshelf().getBooks().get(0)); 

        library.returnBook(reader1, library.getBookshelf().getBooks().get(0)); 

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("library_data.dat"))) {
            library.writeExternal(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LibrarySystem restoredLibrary = new LibrarySystem();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("library_data.dat"))) {
            restoredLibrary.readExternal(inputStream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Restored Library Bookshelf: " + restoredLibrary.getBookshelf());
        System.out.println("Restored Library Rental: " + restoredLibrary.getRental());
    }
}




