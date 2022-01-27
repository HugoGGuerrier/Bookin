package fr.bookin.bookin_back.database.index;

import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.database.index.mongo.*;
import fr.bookin.bookin_back.database.models.Book;
import fr.bookin.bookin_back.database.splitters.Splitters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * This class represents the index database using a mongoDB connection to store it
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class MongoIndexDb implements IndexDb {

    // ===== Macros =====


    /** The class logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoIndexDb.class);


    // ===== Attributes =====


    /** The mongodb repository to store the index */
    @Autowired
    private MongoIndexRepository indexRepository;

    /** The mongodb repository to store the information */
    @Autowired
    private MongoInfoRepository infoRepository;

    /** The mongodb template */
    @Autowired
    private MongoTemplate mongoTemplate;


    // ===== Constructors =====


    /**
     * Create a new mongo index database with the wanted repository
     */
    public MongoIndexDb() {}


    // ===== Override methods =====


    /** @see IndexDb#init(Database) */
    @Override
    public void init(Database database) {
        // Prepare the working vars
        List<Book> books = database.getBooks();
        String bookDir = database.getBooksDirectory();
        int currentBook = 1;

        // Log the indexing because it's a big operation
        LOGGER.info("Start the indexing...");

        for(Book book : books) {

            // Display the progression
            System.out.print("Progress : " + currentBook + "/" + books.size() + "\r");
            System.out.flush();

            // Insert the book in the index
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

    /** @see IndexDb#getWords() */
    @Override
    public Set<String> getWords() {
        // Prepare the result
        Set<String> words = new HashSet<>();

        // Get the list of words from the database
        List<MongoIndex> wordViews = indexRepository.findAll();
        for(MongoIndex wordView : wordViews) {
            words.add(wordView.getWord());
        }

        // Return the result
        return words;
    }

    /** @see IndexDb#getAssociatedDocuments(String) */
    @Override
    public Map<Integer, Integer> getAssociatedDocuments(String word) {
        Optional<MongoIndex> indexOptional = indexRepository.findById(word.hashCode());
        return indexOptional.map(MongoIndex::getIndexMap).orElse(null);
    }

    /** @see IndexDb#indexBook(Book, File) */
    @Override
    public void indexBook(Book book, File bookFile) throws IOException {
        // Get the mongo information and add the book id to it
        MongoInfo info = infoRepository.findById(MongoInfo.CURRENT_ID).orElseGet(MongoInfo::new);
        if(info.getIndexedBooks().contains(book.getId())) {
            return;
        }
        info.getIndexedBooks().add(book.getId());
        mongoTemplate.save(info);

        // Get the book content in a string
        String bookContent = Files.readString(bookFile.toPath());

        // Get the book words and add it to the index table
        String[] words = Splitters.getSplitter(book.getLang()).split(bookContent);

        for(String word : words) {
            // Set the word to the lower case
            word = word.toLowerCase(Locale.ROOT);

            // Get the index in the database or create one if it doesn't exist
            Optional<MongoIndex> indexOptional = indexRepository.findById(word.hashCode());
            MongoIndex index;
            if(indexOptional.isPresent()) {
                index = indexOptional.get();
            } else {
                index = new MongoIndex();
                index.setId(word.hashCode());
                index.setWord(word);
            }

            // Add a unit to the book word counter
            Map<Integer, Integer> indexMap = index.getIndexMap();
            indexMap.put(book.getId(), indexMap.getOrDefault(book.getId(), 0) + 1);

            // Update the database
            mongoTemplate.save(index);
        }
    }

}
