package fr.bookin.bookin_back.controllers;

import fr.bookin.bookin_back.database.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handle all missed request on the server
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    // ===== Attributes =====


    /** The database singleton */
    @Autowired
    private Database database;


    // ===== HTTP methods =====


    /**
     * This methods handle error request
     *
     * @return The HTTP response for a wrong request
     */
    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        // TODO : Log the request

        // Return the response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Page not found...");
    }

}
