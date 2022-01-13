package fr.bookin.bookin_back.database;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

/**
 * This class represent the POJO for the index in database
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@Document(collection = "indexes", schemaVersion = "1.0")
public class Index {

    // ===== Attributes =====


    @Id
    private long id;


    // ===== Getters =====


    public long getId() {
        return id;
    }


    // ===== Setters =====


    public void setId(long id) {
        this.id = id;
    }

}
