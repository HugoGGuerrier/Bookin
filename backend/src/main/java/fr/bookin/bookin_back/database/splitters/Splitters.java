package fr.bookin.bookin_back.database.splitters;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class store all text splitters
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class Splitters {

    // ===== Macros =====


    /** The init status */
    private static boolean init = false;

    /** The splitters map */
    private static final Map<String, ISplitter> splitters = new HashMap<>();


    // ===== Class methods =====


    /**
     * Initialize the splitters map
     */
    private static void init() {
        splitters.put("fr", new FrenchSplitter());
        init = true;
    }

    /**
     * Get the splitter for a specific lang
     *
     * @param lang The lang
     * @return The splitter
     */
    public static ISplitter getSplitter(String lang) {
        if(!init) init();
        return splitters.getOrDefault(lang.toLowerCase(Locale.ROOT), new DefaultSplitter());
    }

}
