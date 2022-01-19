package fr.bookin.bookin_back.database.index.mongo;

import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an index entry in the mongoDB database
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class MongoIndex {

    // ===== Attributes =====


    @Id
    private int id;
    private String word;
    private Map<Integer, Integer> indexMap;


    // ===== Constructors =====


    public MongoIndex() {
        this.id = 0;
        this.word = null;
        this.indexMap = new HashMap<>();
    }


    // ===== Getters =====


    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public Map<Integer, Integer> getIndexMap() {
        return indexMap;
    }


    // ===== Setters =====


    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setIndexMap(Map<Integer, Integer> indexMap) {
        this.indexMap = indexMap;
    }

}
