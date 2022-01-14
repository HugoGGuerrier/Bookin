package fr.bookin.bookin_back;

import fr.bookin.bookin_back.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class get together all util functions
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class Utils {

    // ===== Macros =====


    /** The hash algorithm */
    public static final String HASH_ALGO = "SHA-512";

    /** The logger */
    public static final Logger LOGGER = LoggerFactory.getLogger(Database.class);


    // ===== Utils functions =====


    public static String hashString(String toHash) {
        // Prepare the result
        String res = null;

        try {
            // Hash the message
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGO);
            byte[] bytes = messageDigest.digest(toHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            res = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error in message hashing", e);
        }

        return res;
    }


}
