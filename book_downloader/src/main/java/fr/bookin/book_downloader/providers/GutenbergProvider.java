package fr.bookin.book_downloader.providers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    /** The number of book per result page */
    private static final short BOOK_P_PAGE = 32;


    // ===== Attributes =====


    /** The provider configuration */
    private ProviderConfig config;

    /** The HTTP client for the API requesting */
    private final HttpClient client;

    /** There is a next request page */
    private boolean hasNext;


    // ===== Constructors =====


    /**
     * Create a new gutenberg project provider
     */
    public GutenbergProvider() {
        this.client = HttpClient.newHttpClient();
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
        // Prepare the working vars
        int currentPage = 1;
        int bookCount = 0;

        // While there is a next page, continue
        while(hasNext) {

            // Forge the request for the API
            HttpRequest request = HttpRequest.newBuilder(buildBooksURI(currentPage)).build();

            // Set the request to the API and parse the response
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                throw new ProviderException(e);
            }

            System.out.println(response.body());

            hasNext = false;

        }
    }

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

}
