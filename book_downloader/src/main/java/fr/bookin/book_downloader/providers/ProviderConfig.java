package fr.bookin.book_downloader.providers;

import fr.bookin.book_downloader.Config;

import java.util.List;

/**
 * This class represent a configuration for a provider
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ProviderConfig {

    // ===== Attributes =====


    /** The export path */
    private String exportPath;

    /** The possible language */
    private List<String> lang;

    /** The minimum word count for a book, default value is 0 */
    private long minWordCount;

    /** The number of book to download */
    private long bookCount;


    // ===== Constructors =====


    /**
     * Create a new provider config with all needed parameters
     *
     * @param exportPath The export path
     * @param lang The lang list
     * @param minWordCount The minimum word count
     * @param bookCount The number of book to download, -1 is for all books
     */
    public ProviderConfig(String exportPath, List<String> lang, long minWordCount, long bookCount) {
        this.exportPath = exportPath;
        this.lang = lang;
        this.minWordCount = minWordCount;
        this.bookCount = bookCount;
    }

    /**
     * Create a new provider config with the default value
     */
    public ProviderConfig() {
        this(null, null, 0, -1);
    }

    /**
     * Create a new provider config with the app config object
     *
     * @param config The application config
     */
    public ProviderConfig(Config config) {
        this(config.getExportPath(), config.getLang(), config.getMinWordCount(), config.getBookCount());
    }


    // ===== Getters =====


    public String getExportPath() {
        return exportPath;
    }

    public List<String> getLang() {
        return lang;
    }

    public long getMinWordCount() {
        return minWordCount;
    }

    public long getBookCount() {
        return bookCount;
    }


    // ===== Setters =====


    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public void setMinWordCount(long minWordCount) {
        this.minWordCount = minWordCount;
    }

    public void setBookCount(long bookCount) {
        this.bookCount = bookCount;
    }

}
