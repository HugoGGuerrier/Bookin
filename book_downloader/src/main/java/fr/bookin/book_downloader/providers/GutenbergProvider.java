package fr.bookin.book_downloader.providers;

import fr.bookin.book_downloader.Book;
import io.jsondb.JsonDBTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the implementation of a provider for the Gutenberg project via the Gutendex online API
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GutenbergProvider implements IProvider {

    // ===== Macros =====


    /** The base URL of the Gutendex API */
    private static final String API_URL = "https://gutendex.com/";

    /** The API endpoint for the book request */
    private static final String API_BOOK_ENDPOINT = "books/";

    /** The downloading directory */
    private static final String DOWNLOAD_DIR = "gutenberg/";

    /** The books directory */
    private static final String BOOK_DIR = "books/";


    // ===== Attributes =====


    /** The provider configuration */
    private ProviderConfig config;

    /** The HTTP client for the API requesting */
    private final HttpClient client;

    /** There is a next request page */
    private boolean hasNext;

    /** The JSON DB connector */
    private JsonDBTemplate jsonDB;


    // ===== Constructors =====


    /**
     * Create a new gutenberg project provider
     */
    public GutenbergProvider() {
        this.client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        this.hasNext = true;
    }


    // ===== Class methods =====


    /** @see IProvider#setProviderConfig(ProviderConfig) */
    @Override
    public void setProviderConfig(ProviderConfig config) {
        this.config = config;
    }

    /** @see IProvider#download() */
    @Override
    public void download() throws ProviderException {
        // Create the book directory
        File bookDir = new File(config.getExportPath() + DOWNLOAD_DIR + BOOK_DIR);
        if(!bookDir.exists() || !bookDir.isDirectory()) {
            if(!bookDir.mkdirs()) {
                throw new ProviderException("Cannot create the download folder");
            }
        }

        // Create the JSON DB connector
        jsonDB = new JsonDBTemplate(config.getExportPath() + DOWNLOAD_DIR, "fr.bookin.book_downloader");

        // Create the book collection
        if(!jsonDB.collectionExists(Book.class)) {
            jsonDB.createCollection(Book.class);
        }


        // Prepare the working vars
        int currentPage = 1;
        long bookCount = 0;
        JSONParser jsonParser = new JSONParser();

        // While there is a next page, continue
        while(hasNext && bookCount + 1 <= config.getBookCount()) {

            // Forge the request for the API
            HttpRequest request = HttpRequest.newBuilder(buildBooksURI(currentPage)).build();

            // Set the request to the API and parse the response
            try {

                // Get the response and parse the JSON
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject booksJson = (JSONObject) jsonParser.parse(response.body());

                // Get the book list
                JSONArray bookArray = (JSONArray) booksJson.getOrDefault("results", new JSONArray());
                for(Object object : bookArray) {
                    // Get the json for the book
                    JSONObject bookJson = (JSONObject) object;

                    // Download the book
                    if(downloadBook(bookJson, bookCount)) bookCount++;

                    // Verify the book count
                    if(bookCount + 1 >= config.getBookCount()) break;
                }

                // Get the next page
                if(booksJson.getOrDefault("next", null) != null) {
                    currentPage++;
                } else {
                    hasNext = false;
                }

            } catch (Exception e) {
                throw new ProviderException(e);
            }

        }
    }

    /**
     * Build the book URI from the current page
     *
     * @param page The current page
     * @return The URI of the page
     */
    private URI buildBooksURI(int page) {
        // Create the lang string parameter
        String langString = String.join(",", config.getLang());

        // Create the parameters array
        List<String> paramList = new LinkedList<>();
        if(!langString.isEmpty()) paramList.add("languages=" + langString);
        paramList.add("page=" + page);

        // Create the uri and return it
        return URI.create(API_URL + API_BOOK_ENDPOINT + "?" +
                String.join("&", paramList)
        );
    }

    /**
     * Download the book from the passed JSON
     *
     * @param bookJson The JSON of the book
     */
    private boolean downloadBook(JSONObject bookJson, long id) {
        // Get the book information
        String title = (String) bookJson.getOrDefault("title", "Unknown title");
        String lang = (String) ((JSONArray) bookJson.get("languages")).get(0);

        // Get the book authors
        List<String> authors = new ArrayList<>();
        for(Object object : (JSONArray) bookJson.getOrDefault("authors", new JSONArray())) {
            JSONObject jsonObject = (JSONObject) object;
            authors.add((String) jsonObject.getOrDefault("name", "John Doe"));
        }

        // Create the download URLs
        String downloadUrl1 = "https://www.gutenberg.org/files/{id}/{id}-0.txt";
        String downloadUrl2 = "https://www.gutenberg.org/cache/epub/{id}/pg{id}.txt";

        // Get the book id and test it
        Long bookId = (Long) bookJson.getOrDefault("id", null);
        if(bookId == null) {
            System.err.println("Id of the current book is invalid : \"" + title + "\" - SKIPPING...");
            return false;
        }

        // Create the requests
        downloadUrl1 = downloadUrl1.replace("{id}", bookId.toString());
        downloadUrl2 = downloadUrl2.replace("{id}", bookId.toString());
        HttpRequest request1 = HttpRequest.newBuilder(URI.create(downloadUrl1)).build();
        HttpRequest request2 = HttpRequest.newBuilder(URI.create(downloadUrl2)).build();

        // Download the book
        System.out.append("Book ").append(String.valueOf(bookId)).append(" : \"").append(title).append("\" - DOWNLOADING...");
        System.out.flush();

        try {
            // Get the first response
            HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

            // Verify the response status
            if(response.statusCode() > 400) {

                // Get the second response
                response = client.send(request2, HttpResponse.BodyHandlers.ofString());
                if(response.statusCode() > 400) throw new ProviderException("Cannot download the book in plain text");
            }

            // Verify the response word count
            if(response.body().split(" ").length < config.getMinWordCount()) throw new ProviderException("Not enough words");

            // Write the file
            File downloadFile = new File(config.getExportPath() + DOWNLOAD_DIR + BOOK_DIR + id + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(downloadFile));
            writer.write(response.body());

            System.out.append(" DONE !\n");
            System.out.flush();
        } catch (Exception e) {
            System.out.append(" FAILED ! ");
            System.out.println(e.getMessage());
            return false;
        }

        // Create a book POJO and insert it in the database
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthors(authors);
        book.setLang(lang);
        jsonDB.upsert(book);

        // Return the success
        return true;
    }

}
