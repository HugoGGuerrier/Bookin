package fr.bookin.bookin_back.controllers;

import fr.bookin.bookin_back.database.Book;
import fr.bookin.bookin_back.database.Database;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.LinkedList;

/**
 * This controller handle all request for the books manipulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
@RequestMapping("/api/books")
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
    ResponseEntity<String> getBooks(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "advanced", required = false) String advancedString
    ) {
        // Cast the advanced parameter to a boolean
        boolean advanced = Boolean.parseBoolean(advancedString);

        // TODO
        return null;
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

            // Create the new book
            Book newBook = database.addBook(title, authors, lang);

            // Verify the file type
            if("text/plain".equals(file.getContentType())) {
                // Save the file
                File newFile = new File(database.getBooksDirectory() + "/" + newBook.getId() + ".txt");

                return ResponseEntity.ok("{success:true}");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("{success:false, msg='Server only accept plain text files for now'}");
            }
        }

        // Return the unauthorized message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{success:false, msg='You are not an admin'}");
    }

}
