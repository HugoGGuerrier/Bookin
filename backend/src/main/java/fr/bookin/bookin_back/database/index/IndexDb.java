package fr.bookin.bookin_back.database.index;

import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.database.models.Book;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * This interface represents the signature for the index database implementation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface IndexDb {
    /**
     * Initialise the index database
     */
    void init(Database database);

    /**
     * Get a set of all indexed words
     *
     * @return All indexed words
     */
    Set<String> getWords();

    /**
     * Get the index map for a given word
     *
     * @param word The word
     * @return The index map
     */
    Map<Integer, Integer> getIndex(String word);

    /**
     * Index the given book and update it with the word count
     *
     * @param book The book object
     * @param bookFile The book file
     * @throws IOException If the book file cannot be readen
     */
    void indexBook(Book book, File bookFile) throws IOException;
}
