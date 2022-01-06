package fr.bookin.bookin_back;

import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.exceptions.StartupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The initialisation component of the spring boot application
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@Component
public class Init {

    // ===== Attributes =====


    /** The database singleton */
    @Autowired
    private Database database;


    // ===== Class methods =====


    /**
     * The initialisation method, it should be executed just after bean instantiation
     */
    @PostConstruct
    public void init() {
        try {

            // Initialise the database
            database.init();

        } catch (Exception e) {
            // Throw a new un-catchable exception to fail startup
            throw new StartupException(e);
        }
    }

}
