package fr.bookin.bookin_back.controllers;

import fr.bookin.bookin_back.Utils;
import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.database.User;
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
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("{success:true}");
            } else {
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("{success:false, msg='Already logged in'}");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{success:false, msg='Wrong mail or password'}");
        }
    }


    /**
     * Delete a session
     *
     * @param request The HTTP request
     * @return The response in a string
     */
    @DeleteMapping("")
    ResponseEntity<String> logout(HttpServletRequest request) {
        // Verify the session
        if(request.getSession(false) != null && request.getSession(false).getAttribute("user") != null) {
            request.getSession().invalidate();
            return ResponseEntity.ok("{success:true}");
        }

        // Return the unauthorized message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{success:false, msg='You are not an admin'}");
    }

}
