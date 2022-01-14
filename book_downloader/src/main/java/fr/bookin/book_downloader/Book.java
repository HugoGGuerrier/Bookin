package fr.bookin.book_downloader;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a book POJO, it MUST BE synchronized with the Bookin backend class
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


    // ===== Constructors =====


    public Book() {
        this.authors = new ArrayList<>();
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

}
