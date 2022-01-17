package fr.bookin.bookin_back.database.splitters;

/**
 * This interface defines the signature of a text splitter
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface ISplitter {
    /**
     * Split a text into words
     *
     * @param text The text to split
     * @return The words in an array
     */
    String[] split(String text);
}
