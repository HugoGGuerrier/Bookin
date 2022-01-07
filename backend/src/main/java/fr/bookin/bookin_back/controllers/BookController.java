package fr.bookin.bookin_back.controllers;

import fr.bookin.bookin_back.database.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handle all request for the books manipulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
@RequestMapping("/api/book")
public class BookController {

    // ===== Attributes =====


    /** This is the database singleton */
    @Autowired
    private Database database;


    // ===== HTTP methods =====


    /**
     * This method is used for a book research
     *
     * @param query The query text
     * @param advancedString If the query should be executed in advanced mode
     * @return The JSON response of the search
     */
    @GetMapping("")
    String test(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "advanced", required = false) String advancedString
    ) {
        // Cast the advanced parameter to a boolean
        boolean advanced = Boolean.parseBoolean(advancedString);

        if(advanced) {
            return "advanced " + query;
        }
        return query;
    }

}
