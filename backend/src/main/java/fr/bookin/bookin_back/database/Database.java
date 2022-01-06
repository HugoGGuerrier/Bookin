package fr.bookin.bookin_back.database;

import fr.bookin.bookin_back.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * This class represents the database communication singleton
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class Database {

    // ===== Attributes =====


    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    /** The database base directory, it should contains all needed data (books and json files) */
    private String baseDir;


    // ===== Constructors =====


    /**
     * Create a new database with the wanted attributes
     *
     * @param baseDir The base directory
     */
    public Database(String baseDir) {
        this.baseDir = baseDir;
    }


    // ===== Methods =====


    /**
     * Verify that the database configuration is valid :
     *  - There is a directory 'data' inside the base directory
     *  - There is a directory 'books' inside the 'data' directory
     *
     *  If there is an error in the file structures, this method should correct it
     */
    public void verify() throws DatabaseException {
        // Verify that the data directory exists
        File data = new File(baseDir + "/data/books/");
        if(!data.exists() || !data.isDirectory()) {
            LOGGER.info("Creating the database directories...");

            if(!data.mkdirs()) {
                throw new DatabaseException("Cannot create the 'data' and 'books' directories");
            }
        }
    }

}
