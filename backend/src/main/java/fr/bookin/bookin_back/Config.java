package fr.bookin.bookin_back;

import fr.bookin.bookin_back.database.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains the configuration of the spring boot application
 */
@Configuration
public class Config {
    @Bean
    public Database getDatabase() {
        // Create the database singleton in the invoked dir
        return new Database(System.getProperty("user.dir"));
    }
}
