package fr.bookin.bookin_back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents the controller for the users
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // ===== Macros =====


    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    // ===== Attributes =====


    /** The database connector */
    @Autowired
    private Database database;


    // ===== HTTP methods =====


    /**
     * Get a user by mail or all the user
     *
     * @param mail The mail, if null, the method will only return all users
     * @return The HTTP response with the user in JSON
     */
    @GetMapping("")
    ResponseEntity<String> getUser(
            @RequestParam(name = "mail", required = false) String mail,
            HttpServletRequest request
    ) {
        // Verify the session
        if(request.getSession(false) != null && request.getSession(false).getAttribute("user") != null) {

            // Create the JSON object mapper
            ObjectMapper mapper = new ObjectMapper();
            try {
                if(mail == null) {
                    return ResponseEntity.ok(mapper.writeValueAsString(database.getUsers()));
                } else {
                    return ResponseEntity.ok(mapper.writeValueAsString(database.getUserByMail(mail)));
                }
            } catch (Exception e) {
                LOGGER.error("Error in the objet mapping", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Utils.getJsonResponse(false, "Error in the server"));
            }

        }

        // Return the unauthorized message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utils.getJsonResponse(false, "You are not an admin"));
    }

}
