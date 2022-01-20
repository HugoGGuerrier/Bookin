package fr.bookin.bookin_back.database;

import fr.bookin.bookin_back.database.index.IndexDb;
import fr.bookin.bookin_back.database.index.MongoIndexDb;
import fr.bookin.bookin_back.database.index.PureIndexDb;
import fr.bookin.bookin_back.database.models.Book;
import fr.bookin.bookin_back.database.models.User;
import fr.bookin.bookin_back.utils.Utils;
import fr.bookin.bookin_back.exceptions.DatabaseException;
import io.jsondb.JsonDBTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /** The index database connection */
    private IndexDb indexDb;

    /** If the index is going to be stored */
    @Value("${app.store_index}")
    private boolean storeIndex;

    /** The max book ID */
    private int nextBookId;

    /** The pure index database singleton */
    @Autowired
    private PureIndexDb pureIndexDb;

    /** The mongo db index database singleton */
    @Autowired
    private MongoIndexDb mongoIndexDb;


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
        this.indexDb = null;
        this.nextBookId = 0;
    }


    // ===== Getters =====


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
            scanner.close();

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
        if(books.size() == 0) nextBookId = 0;
        else nextBookId = books.get(0).getId() + 1;
    }

    /**
     * Get the proportion of a count in a given book
     *
     * @param bookId The wanted book ID
     * @param count The word count
     * @return The proportion in a double
     */
    private double getProportion(int bookId, int count) {
        Book book = jsonDb.findById(bookId, Book.class);
        return ((double) count) / ((double) book.getWordCount());
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

            // Create the index database connection
            indexDb = storeIndex ? mongoIndexDb : pureIndexDb;

            // Verify that there is a super admin
            verifySuperAdmin();

            // Get the next IDs once for all
            getNextIds();

            // Initialize the index database
            indexDb.init(this);
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
     * Internal method to perform a simple query on the index table
     *
     * @param query The query string
     * @return The map of book ids associated with the word count
     */
    private Map<Integer, Integer> makeSimpleQuery(String query) {
        // Prepare the working var
        Map<Integer, Integer> resultMap = new HashMap<>();
        boolean multiple = false;

        // Split the query into words
        query = query.toLowerCase(Locale.ROOT);
        String[] words = query.split(" ");

        // Iterate over words and get the associated books
        for(String word : words) {
            // Test if the word is long enough
            if(word.length() <= 2) continue;

            // Get the index map
            Map<Integer, Integer> indexMap = indexDb.getAssociatedDocuments(word);

            // If this is the first query word
            if(!multiple) {
                if(indexMap != null) resultMap.putAll(indexMap);
                multiple = true;
            }

            // Else
            else {
                // If the index is null, just return an empty map, no need to go further
                if(indexMap == null) {
                    resultMap.clear();
                    return resultMap;
                }

                // Otherwise, do a meet operation between result and index
                else {
                    for(int bookId : new HashSet<>(resultMap.keySet())) {
                        if(!indexMap.containsKey(bookId)) {
                            resultMap.remove(bookId);
                        } else {
                            resultMap.put(bookId, (resultMap.get(bookId) + indexMap.get(bookId)) / 2);
                        }
                    }
                }
            }
        }

        // Return the result
        return resultMap;
    }

    /**
     * Internal method to perform an advanced query in the index table
     *
     * @param regex The regular expression
     * @return The map of books containing a word matching the regex
     * @throws DatabaseException If there is an error in the regex
     */
    private Map<Integer, Integer> makeAdvancedQuery(String regex) throws DatabaseException {
        // Prepare the working var
        Map<Integer, Integer> resultMap = new HashMap<>();

        // Compile the regular expression to increase performance
        Pattern pattern = Pattern.compile(regex);

        // For each indexed word, verify if it matches the regular expression
        for(String word : indexDb.getWords()) {
            try {
                Matcher m = pattern.matcher(word);
                if(m.matches()) {
                    // Add all count to the result and pondered it
                    for(Map.Entry<Integer, Integer> entry : indexDb.getAssociatedDocuments(word).entrySet()) {
                        resultMap.put(entry.getKey(), (resultMap.getOrDefault(entry.getKey(), entry.getValue()) + entry.getValue()) / 2);
                    }
                }
            } catch (Exception e) {
                throw new DatabaseException(e);
            }
        }

        // Return the result
        return resultMap;
    }


    /**
     * Get all books in the database
     *
     * @return The list with all the books
     */
    public List<Book> getBooks() {
        return jsonDb.findAll(Book.class);
    }

    /**
     * This method is used to get books from a query string in advanced mode or not
     *
     * @param query The query string
     * @param advanced If the search is in advanced mode
     * @return The list of books
     * @throws DatabaseException If there is an error in the server interrogation
     */
    public List<Book> getBooksQuery(String query, boolean advanced) throws DatabaseException {
        // Prepare the result
        Map<Integer, Integer> resultMap = advanced ? makeAdvancedQuery(query) : makeSimpleQuery(query);

        // Transform the result map into a sorted list
        // The list sorting is done with the jaccard distance
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(resultMap.entrySet());
        entryList.sort((e1, e2) ->
                Double.compare(getProportion(e2.getKey(), e2.getValue()), getProportion(e1.getKey(), e1.getValue())));

        // Get the books
        List<Book> res = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : entryList) {
            res.add(jsonDb.findById(entry.getKey(), Book.class));
        }

        // Return the result
        return res;
    }

    /**
     * Create a new book in the database
     *
     * @param title The book title
     * @param authors The book authors
     * @param lang The book lang
     * @param bookFile The uploaded file for the book
     */
    public synchronized void addBook(String title, String[] authors, String lang, MultipartFile bookFile) throws IOException {
        // Create the new book
        Book newBook = new Book();
        newBook.setId(nextBookId++);
        newBook.setTitle(title);
        newBook.setLang(lang);
        for(String author : authors) {
            newBook.addAuthor(author);
        }

        // Save the book file
        File newFile = new File(booksDirectory + "/" + newBook.getId() + ".txt");
        OutputStream outputStream = new FileOutputStream(newFile);
        outputStream.write(bookFile.getBytes());
        outputStream.close();

        // Add in the database
        jsonDb.insert(newBook);

        // Update the book index
        indexDb.indexBook(newBook, newFile);
    }

    /**
     * Update a book in the database
     *
     * @param book The book to update
     */
    public synchronized void updateBook(Book book) {
        jsonDb.upsert(book);
    }

}
