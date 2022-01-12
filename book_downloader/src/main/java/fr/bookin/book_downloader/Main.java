package fr.bookin.book_downloader;

import fr.bookin.book_downloader.providers.GutenbergProvider;
import fr.bookin.book_downloader.providers.IProvider;
import fr.bookin.book_downloader.providers.ProviderConfig;
import fr.bookin.book_downloader.providers.ProviderException;

/**
 * Main entry point
 */
public class Main {
    public static void main(String[] args) {

        // Load the configuration
        System.out.println("Load config...");
        Config config = Config.getInstance();

        // Get the wanted provider from the config
        IProvider provider = null;
        switch (config.getProvider()) {
            case "gutenberg":
                System.out.println("Provider is the Gutenberg Project...");
                provider = new GutenbergProvider();
                break;

            default:
                System.err.println("No valid provider given, please read the manual and retry...");
                System.exit(1);
        }

        // Set the provider configuration
        provider.setProviderConfig(new ProviderConfig(config));

        // Start the downloading
        try {
            System.out.println("Start the downloading...");
            provider.download();
        } catch (ProviderException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
