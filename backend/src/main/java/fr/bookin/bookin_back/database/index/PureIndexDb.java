package fr.bookin.bookin_back.database.index;

import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.database.models.Book;
import fr.bookin.bookin_back.database.splitters.Splitters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * This class contains the index database implementation with a simple java variable
 * 
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PureIndexDb implements IndexDb {

    // ===== Macros =====


    /** The class logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PureIndexDb.class);


    // ===== Attributes =====


    /** The book index table */
    private final Map<String, Map<Integer, Integer>> indexTable;


    // ===== Constructors =====


    public PureIndexDb() {
        this.indexTable = new HashMap<>();
    }


    // ===== Class methods =====


    /** @see IndexDb#init(fr.bookin.bookin_back.database.Database) */
    @Override
    public void init(Database database) {
        // Init the working vars
        List<Book> books = database.getBooks();
        String bookDir = database.getBooksDirectory();
        int currentBook = 1;

        // Log the indexing because it's a big operation
        LOGGER.info("Start the indexing...");

        // For each book add words in the index table
        for(Book book : books) {

            // Display the progression
            System.out.print("Progress : " + currentBook + "/" + books.size() + "\r");
            System.out.flush();

            // Add the book
            try {
                indexBook(book, new File(bookDir + "/" + book.getId() + ".txt"));
            } catch (IOException e) {
                LOGGER.error("Cannot open a book file", e);
            }

            // Increase the book count
            currentBook++;
        }

        // Log the indexing end
        LOGGER.info("Indexing done !");
    }

    /**@see IndexDb#getWords() */
    @Override
    public Set<String> getWords() {
        return indexTable.keySet();
    }

    /** @see IndexDb#getAssociatedDocuments(String) */
    @Override
    public Map<Integer, Integer> getAssociatedDocuments(String word) {
        return indexTable.get(word);
    }

    /** @see IndexDb#indexBook(Book, File) */
    @Override
    public void indexBook(Book book, File bookFile) throws IOException {
        // Get the book content in a string
        String bookContent = Files.readString(bookFile.toPath());

        // Get the book words and add it to the index table
        String[] words = Splitters.getSplitter(book.getLang()).split(bookContent);

        for(String word : words) {
            // Set the word to the lower case
            word = word.toLowerCase(Locale.ROOT);

            // Get the word count table or an empty table if it does not exist
            Map<Integer, Integer> wordCountTable = getAssociatedDocuments(word);
            if(wordCountTable == null) wordCountTable = new HashMap<>();
            wordCountTable.put(book.getId(), wordCountTable.getOrDefault(book.getId(), 0) + 1);

            // Put the count table if absent from the index table
            indexTable.put(word, wordCountTable);
        }
    }

}
