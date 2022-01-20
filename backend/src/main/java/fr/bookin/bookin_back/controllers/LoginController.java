package fr.bookin.bookin_back.controllers;

import fr.bookin.bookin_back.utils.Utils;
import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.database.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the login for the login service
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    // ===== Attributes =====


    /** The database plugin */
    @Autowired
    private Database database;


    // ===== HTTP methods =====


    /**
     * HTTP method to get if the client is logged in
     *
     * @return The HTTP response containing the log status of the client
     */
    @GetMapping("")
    ResponseEntity<String> isLoggedIn(HttpServletRequest request) {
        if(request.getSession(false) != null && request.getSession(false).getAttribute("user") != null) {
            return ResponseEntity.ok(Utils.getJsonResponse(true, null));
        }
        return ResponseEntity.ok(Utils.getJsonResponse(false, null));
    }

    /**
     * Login a user by the mail and password
     *
     * @param mail The user mail
     * @param pass The user password
     * @param request The HTTP request
     * @return The HTTP response
     */
    @PostMapping("")
    ResponseEntity<String> login(
            @RequestParam(name = "mail") String mail,
            @RequestParam(name = "pass") String pass,
            HttpServletRequest request
    ) {
        // Hash the password
        String hashedPass = Utils.hashString(pass);

        // Try getting the user
        User user = database.loginUser(mail, hashedPass);
        if(user != null) {
            if(request.getSession().getAttribute("user") == null) {
                request.getSession().setAttribute("user", user);
                return ResponseEntity.ok(Utils.getJsonResponse(true, null));
            } else {
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Utils.getJsonResponse(false, "You are already logged in"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utils.getJsonResponse(false, "Wrong mail or password"));
        }
    }


    /**
     * Delete a session
     *
     * @param request The HTTP request
     * @return The response in a string
     */
    @CrossOrigin(origins = "localhost:9000")
    @DeleteMapping("")
    ResponseEntity<String> logout(HttpServletRequest request) {
        // Verify the session
        if(request.getSession(false) != null && request.getSession(false).getAttribute("user") != null) {
            request.getSession().invalidate();
            return ResponseEntity.ok(Utils.getJsonResponse(true, null));
        }

        // Return the unauthorized message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utils.getJsonResponse(false, "You are already logged in"));
    }

}
