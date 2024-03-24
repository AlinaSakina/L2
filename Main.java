import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;
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

class Reader implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Reader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "name='" + name + '\'' +
                '}';
    }
}

class Library implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Book> books;
    private List<Reader> readers;

    public Library() {
        books = new ArrayList<>();
        readers = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    public void displayStatus() {
        System.out.println("Books in the library:");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("\nReaders in the library:");
        for (Reader reader : readers) {
            System.out.println(reader);
        }
    }

    public Book lendBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                books.remove(book);
                return book;
            }
        }
        return null;
    }
}

public class Main {
    public static void serializeObject(String fileName, Object obj) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(obj);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deSerializeObject(String fileName) {
        Object obj = null;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            obj = is.readObject();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args) {
        try {
            Library library = new Library();

            library.addBook(new Book("Harry Potter", "J.K. Rowling"));
            library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
            library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
            library.addBook(new Book("1984", "George Orwell"));

            library.addReader(new Reader("Alice"));
            library.addReader(new Reader("Bob"));
            library.addReader(new Reader("Charlie"));

            System.out.println("Initial library status:");
            library.displayStatus();

            String bookTitle = "Harry Potter";
            System.out.println("\nLending \"" + bookTitle + "\" to a reader...");
            Book lentBook = library.lendBook(bookTitle);
            if (lentBook != null) {
                System.out.println("Book \"" + lentBook.getTitle() + "\" lent successfully.");
            } else {
                System.out.println("Book \"" + bookTitle + "\" not found in the library.");
            }

            serializeObject("library.ser", library);

            Library deserializedLibrary = (Library) deSerializeObject("library.ser");

            System.out.println("\nDeserialized library status:");
            deserializedLibrary.displayStatus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

