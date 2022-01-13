package fr.bookin.bookin_back.database;

import fr.bookin.bookin_back.Utils;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

/**
 * This class is a POJO for site admin
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@Document(collection = "users", schemaVersion = "1.0")
public class User {

    // ===== Attributes =====

    @Id
    private String mail;
    private String password;
    private int level;


    // ===== Getters =====


    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }


    // ===== Setters =====


    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    // ===== Override methods =====


    @Override
    public String toString() {
        return "User{" +
                " mail=" + mail +
                " password=" + password +
                " level=" + level +
                "}";
    }

}
