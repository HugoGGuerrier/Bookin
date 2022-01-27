package fr.bookin.bookin_back.database.models;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a book POJO, it MUST BE synchronized with the Bookin book downloader class
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@Document(collection = "books", schemaVersion = "1.0")
public class Book {

    // ===== Attributes =====

    @Id
    private int id;

    private String title;
    private List<String> authors;
    private String lang;
    private long wordCount;
    private Map<Integer, Double> distances;


    // ===== Constructors =====


    public Book() {
        this.title = null;
        this.authors = new ArrayList<>();
        this.lang = null;
        this.wordCount = 0;
        this.distances = new HashMap<>();
    }


    // ===== Getters =====


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getLang() {
        return lang;
    }

    public long getWordCount() {
        return wordCount;
    }

    public Map<Integer, Double> getDistances() {
        return distances;
    }


    // ===== Setters =====


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void removeAuthor(String author) {
        this.authors.remove(author);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }

    public void setDistances(Map<Integer, Double> distances) {
        this.distances = distances;
    }


    // ===== Override methods =====


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                " title=" + title +
                " authors=" + authors +
                " lang=" + lang +
                " wordCount=" + wordCount +
                " distances=" + distances +
                "}";
    }

}