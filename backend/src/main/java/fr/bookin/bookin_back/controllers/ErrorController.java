package fr.bookin.bookin_back.controllers;

import fr.bookin.bookin_back.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * This class handle all missed request on the server
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    // ===== Macros =====


    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);


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
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        LOGGER.info("Error request on the server : " + request.getRequestURI());

        // Return the response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Page not found...");
    }

}
