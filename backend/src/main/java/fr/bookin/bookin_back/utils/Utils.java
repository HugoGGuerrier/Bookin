package fr.bookin.bookin_back.utils;

import fr.bookin.bookin_back.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class get together all util functions to run the Bookin website
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

    /**
     * Return the JSON string for the response
     * 
     * @param success If the action is a success
     * @param msg The message, it can be null
     * @return The JSON string
     */
    public static String getJsonResponse(boolean success, String msg) {
        return "{\"success\" : " + String.valueOf(success) + 
            (msg == null ? "" : ", \"msg\" : \"" + msg + "\"") +
            "}";
    }

    /**
     * Get the jaccard distance between the two given texts
     *
     * @param text1 The text 1
     * @param text2 The text 2
     * @return The jaccard distance between 0 and 1
     */
    public static double getDistance(String[] text1, String[] text2) {
        // Prepare the working vars
        Map<String, Integer> index1 = new HashMap<>();
        Map<String, Double> prop1 = new HashMap<>();
        Map<String, Integer> index2 = new HashMap<>();
        Map<String, Double> prop2 = new HashMap<>();
        double res = 0.0d;

        // Get the index for the first text
        for(String word : text1) {
            word = word.toLowerCase(Locale.ROOT);
            index1.put(word, index1.getOrDefault(word, 0) + 1);
        }

        // Create the proportion for index 1
        for(Map.Entry<String, Integer> entry : index1.entrySet()) {
            prop1.put(entry.getKey(), ((double) entry.getValue()) / ((double) text1.length));
        }

        // Get the index for the second text
        for(String word : text2) {
            word = word.toLowerCase(Locale.ROOT);
            index2.put(word, index2.getOrDefault(word, 0) + 1);
        }

        // Create the proportion for index 2
        for(Map.Entry<String, Integer> entry : index2.entrySet()) {
            prop2.put(entry.getKey(), ((double) entry.getValue()) / ((double) text2.length));
        }

        // For every word, if it's in the two prop map, add it to the result
        for(String word : prop1.keySet()) {
            if(prop2.containsKey(word)) {
                res += (prop1.get(word) + prop2.get(word)) / 2.0d;
            }
        }

        // Return the result
        return res;
    }


}
