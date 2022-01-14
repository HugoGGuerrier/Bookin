package fr.bookin.bookin_back.database;

import fr.bookin.bookin_back.Utils;
import fr.bookin.bookin_back.exceptions.DatabaseException;
import io.jsondb.JsonDBTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * This class represents the database communication singleton
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class Database {

    // ===== Macros =====


    /** The logger to print message from the database */
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);


    // ===== Attributes =====


    /** The database base directory, it should contain all needed data (books and json files) */
    private final String baseDir;

    /** The database file location */
    private final String databaseFileLocation;

    /** The directory where the books are */
    private final String booksDirectory;

    /** The JSON database connection */
    private JsonDBTemplate jsonDb;

    /** The book index table */
    private final Map<String, Map<Integer, Integer>> indexTable;

    /** The max book ID */
    private int nextBookId;


    // ===== Constructors =====


    /**
     * Create a new database with the wanted attributes
     *
     * @param baseDir The base directory
     */
    public Database(String baseDir) {
        this.baseDir = baseDir;
        this.databaseFileLocation = baseDir + "/data";
        this.booksDirectory = baseDir + "/data/books";
        this.jsonDb = null;
        this.indexTable = new HashMap<>();
        this.nextBookId = 0;
    }


    // ===== Getters =====


    public String getDatabaseFileLocation() {
        return databaseFileLocation;
    }

    public String getBooksDirectory() {
        return booksDirectory;
    }


    // ===== Internal methods =====


    /**
     * Verify that the user collection contains the super admin instance, if there is none, ask the user to create one
     */
    private void verifySuperAdmin() {
        // Get the user that is at the level 0 (super admin)
        String jxQuery = String.format("/.[level=%d]", 0);
        List<User> superAdmins = jsonDb.find(jxQuery, User.class);

        // Test the user list
        if(superAdmins.size() != 1) {
            // Delete the super admins
            jsonDb.remove(superAdmins, User.class);

            // Display the warning
            LOGGER.warn("Cannot find a super admin, please define one : ");

            // Get the user input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Super admin mail : ");
            String mail = scanner.nextLine();
            System.out.print("Super admin password : ");
            String password = scanner.nextLine();
            String hashedPass = Utils.hashString(password);

            // Create the new super admin
            User newSuperAdmin = new User();
            newSuperAdmin.setLevel(0);
            newSuperAdmin.setMail(mail);
            newSuperAdmin.setPassword(hashedPass);
            jsonDb.upsert(newSuperAdmin);
        }
    }

    /**
     * Get the next ids in the database and set it in the database connector to avoid querying it every time
     */
    private void getNextIds() {
        // Get the book next ID
        List<Book> books = jsonDb.findAll(Book.class, (b1, b2) -> Long.compare(b2.getId(), b1.getId()));
        nextBookId = books.get(0).getId() + 1;
    }

    /**
     * Create from the book in the database, the index table and store it in the database
     */
    private void initIndex() {
        // Get all books and prepare the new index table
        List<Book> books = getBooks();
        long currentBook = 1L;

        // Log the indexing because it's a big operation
        LOGGER.info("Start the indexing...");

        // For each book add words in the index table
        for(Book book : books) {

            // Display the progression
            System.out.print("Progress : " + currentBook + "/" + books.size() + "\r");
            System.out.flush();

            // Add the book
            addBookToIndex(book);

            // Increase the book count
            currentBook++;

        }

        // Log the indexing end
        LOGGER.info("Indexing done !");
    }


    // ===== Class methods =====


    /**
     * Verify that the database configuration is valid :
     *  - There is a directory 'data' inside the base directory
     *  - There is a directory 'books' inside the 'data' directory
     *
     *  Create the database connection and initialise it
     *
     *  If there is an error in the file structures, this method should correct it
     */
    public void init() throws DatabaseException {

        // Verify that the data and books directories exists
        File data = new File(baseDir + "/data/books");
        if(!data.exists() || !data.isDirectory()) {
            LOGGER.info("Creating the database directories...");

            if(!data.mkdirs()) {
                throw new DatabaseException("Cannot create the 'data' and 'books' directories");
            }
        }

        try {
            // Create the database connection
            String baseScanPackage = "fr.bookin.bookin_back.database";
            jsonDb = new JsonDBTemplate(databaseFileLocation, baseScanPackage);

            // Create the needed collections
            if(!jsonDb.collectionExists(Book.class)) jsonDb.createCollection(Book.class);
            if(!jsonDb.collectionExists(User.class)) jsonDb.createCollection(User.class);

            // Verify that there is a super admin
            verifySuperAdmin();

            // Get the next IDs once for all
            getNextIds();

            // Initialize the index of all books
            initIndex();
        } catch (Exception e) {
            LOGGER.error("Cannot initialise the database !");
            throw new DatabaseException(e);
        }

    }


    // ===== Database manipulation methods =====


    // --- For User

    /**
     * Get all users
     *
     * @return The users in a list
     */
    public List<User> getUsers() {
        return jsonDb.findAll(User.class);
    }

    /**
     * Get the user by its mail
     *
     * @param mail The user mail
     * @return The user if there is one, null else
     */
    public User getUserByMail(String mail) {
        return jsonDb.findById(mail, User.class);
    }

    /**
     * Get the user by mail and password
     *
     * @param mail The mail
     * @param hashedPassword The hashed password
     * @return A user if the login is valid, null else
     */
    public User loginUser(String mail, String hashedPassword) {
        // Get the user and verify the password
        User user = jsonDb.findById(mail, User.class);
        if(user != null) {
            if(user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }

        // Default response
        return null;
    }

    // --- For Books

    /**
     * Get all books in the database
     *
     * @return The list with all the books
     */
    public List<Book> getBooks() {
        return jsonDb.findAll(Book.class);
    }

    /**
     * Create a new book in the database
     *
     * @param title The book title
     * @param authors The book authors
     * @param lang The book lang
     * @return The newly created book
     */
    public synchronized Book addBook(String title, String[] authors, String lang) {
        // Create the new book
        Book newBook = new Book();
        newBook.setId(nextBookId++);
        newBook.setTitle(title);
        newBook.setLang(lang);
        for(String author : authors) {
            newBook.addAuthor(author);
        }

        // Add in the database
        jsonDb.insert(newBook);

        // Return the book
        return newBook;
    }

    // --- For index

    /**
     * Add a book to the index
     *
     * @param book The book to add
     */
    public void addBookToIndex(Book book) {
        // Verify that the book file exists, and read it
        File bookFile = new File(booksDirectory + "/" + book.getId() + ".txt");
        if(!bookFile.exists()) {
            LOGGER.error("File for book " + book.getId() + " does not exist");
            jsonDb.remove(book, Book.class);
            return;
        }

        String bookContent = "";

        try {
            bookContent = Files.readString(bookFile.toPath());
        } catch (Exception e) {
            LOGGER.error("Cannot open " + book.getId() + " book file", e);
            return;
        }

        // Get the book words and add it to the index table
        bookContent = bookContent.replaceAll("[\\[\\]\"'_=+*/.|{}()~#&%<>`]", " ");
        String[] words = bookContent.split("\\s+");

        for(String word : words) {
            // Set the word to the lower case
            word = word.toLowerCase(Locale.ROOT);

            // Get the word count table of an empty table if it does not exist
            Map<Integer, Integer> wordCountTable = indexTable.getOrDefault(word, new HashMap<>());
            wordCountTable.put(book.getId(), wordCountTable.getOrDefault(book.getId(), 0) + 1);

            // Put the count table if absent from the index table
            indexTable.put(word, wordCountTable);
        }
    }

}
