package fr.bookin.bookin_back.database.index.mongo;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the information about the indexation in the mongodb database
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class MongoInfo {

    // ===== Macros =====


    /** The current ID of the information */
    public static final int CURRENT_ID = 0;


    // ===== Attributes =====


    @Id
    private int id;
    private List<Integer> indexedBooks;


    // ===== Constructors =====


    public MongoInfo() {
        this.id = CURRENT_ID;
        this.indexedBooks = new LinkedList<>();
    }


    // ===== Getters =====


    public int getId() {
        return id;
    }

    public List<Integer> getIndexedBooks() {
        return indexedBooks;
    }


    // ===== Setters =====


    public void setId(int id) {
        this.id = id;
    }

    public void setIndexedBooks(List<Integer> indexedBooks) {
        this.indexedBooks = indexedBooks;
    }


    // ===== Override methods =====


    @Override
    public String toString() {
        return "MongoInfo{" +
                "id=" + id +
                " indexedBooks=" + indexedBooks +
                "}";
    }

}
