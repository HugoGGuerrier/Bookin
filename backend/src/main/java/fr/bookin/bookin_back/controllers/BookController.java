package fr.bookin.bookin_back.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bookin.bookin_back.database.models.Book;
import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.exceptions.DatabaseException;
import fr.bookin.bookin_back.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This controller handle all request for the books manipulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    // ===== Macros =====


    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);


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
    ResponseEntity<String> getBooks(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "advanced", required = false) String advancedString
    ) {
        // Cast the advanced parameter to a boolean
        boolean advanced = Boolean.parseBoolean(advancedString);

        try {
            // Get the book list from the database
            List<Book> books = database.getBooksQuery(query, advanced);

            // Transform the list with the object mapper
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.ok(mapper.writeValueAsString(books));
        } catch (JsonProcessingException e) {
            LOGGER.error("Error in object mapping", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Utils.getJsonResponse(false, "Error in the server"));
        } catch (DatabaseException e) {
            LOGGER.error("Error in the regex pattern", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Utils.getJsonResponse(false, "Error in regex"));
        }
    }

    @PostMapping("")
    ResponseEntity<String> addBook(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "authors") String[] authors,
            @RequestParam(name = "lang") String lang,
            @RequestParam(name = "file") MultipartFile file,
            HttpServletRequest request
    ) {
        // Verify the session
        if(request.getSession(false) != null && request.getSession(false).getAttribute("user") != null) {
            // Verify the file type
            if("text/plain".equals(file.getContentType()) && !file.isEmpty()) {

                try {
                    // Insert the book in the database
                    database.addBook(title, authors, lang, file);

                    return ResponseEntity.ok(Utils.getJsonResponse(true, null));
                } catch (Exception e) {
                    LOGGER.error("Cannot save the book file", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Utils.getJsonResponse(false, "Cannot save the book file"));
                }

            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Utils.getJsonResponse(false, "Server only accept plain text files for now"));
            }
        }

        // Return the unauthorized message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utils.getJsonResponse(false, "You are not an admin"));
    }

}
