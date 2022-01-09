package fr.bookin.book_downloader.providers;

/**
 * This interface represents the signature of every provider
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface IProvider {
    /**
     * Set the provider configuration
     *
     * @param config The provider configuration
     */
    void setProviderConfig(ProviderConfig config);

    /**
     * Start the downloading of the books
     *
     * @throws ProviderException If there is an error during the downloading
     */
    void download() throws ProviderException;
}
