package fr.bookin.bookin_back.database;

import fr.bookin.bookin_back.exceptions.DatabaseException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.Default1Cipher;
import io.jsondb.crypto.ICipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * This class represents the database communication singleton
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class Database {

    // ===== Attributes =====


    /** The logger to print message from the database */
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    /** The database base directory, it should contains all needed data (books and json files) */
    private final String baseDir;

    /** The database file location */
    private final String databaseFileLocation;

    /** The directory where the books are */
    private final String booksDirectory;

    /** The JSON database connection */
    private JsonDBTemplate jsonDb;

    /** The cipher secret key */
    @Value("${cipher.key}")
    private String cipherKey;

    /** The cipher salt value */
    @Value("${cipher.salt}")
    private String cipherSalt;


    // ===== Constructors =====


    /**
     * Create a new database with the wanted attributes
     *
     * @param baseDir The base directory
     */
    public Database(String baseDir) {
        this.baseDir = baseDir;
        this.databaseFileLocation = baseDir + "/data";
        this.booksDirectory = baseDir + "/data/books";
        this.jsonDb = null;
    }


    // ===== Class methods =====


    /**
     * Verify that the database configuration is valid :
     *  - There is a directory 'data' inside the base directory
     *  - There is a directory 'books' inside the 'data' directory
     *
     *  Create the database connection and initialise it
     *
     *  If there is an error in the file structures, this method should correct it
     */
    public void init() throws DatabaseException {

        // Verify that the data and books directories exists
        File data = new File(baseDir + "/data/books");
        if(!data.exists() || !data.isDirectory()) {
            LOGGER.info("Creating the database directories...");

            if(!data.mkdirs()) {
                throw new DatabaseException("Cannot create the 'data' and 'books' directories");
            }
        }

        // Create the database connection
        try {
            String baseScanPackage = "fr.bookin.bookin_back.database";
            ICipher cipher = new Default1Cipher(CryptoUtil.generate128BitKey(cipherKey, cipherSalt));
            jsonDb = new JsonDBTemplate(databaseFileLocation, baseScanPackage, cipher);
        } catch (Exception e) {
            LOGGER.error("Cannot initialise the database !");
            throw new DatabaseException(e);
        }

    }

}
