package fr.bookin.bookin_back;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import fr.bookin.bookin_back.database.Database;
import fr.bookin.bookin_back.database.index.MongoIndexDb;
import fr.bookin.bookin_back.database.index.PureIndexDb;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class contains the configuration of the spring boot application
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@Configuration
@EnableMongoRepositories(basePackages = "fr.bookin.bookin_back.database.index.mongo")
public class Config {

    // ===== Attributes =====


    @Value(value = "${app.debug}")
    private boolean debug;

    @Value(value = "${mongo.db_name}")
    private String mongoDbName;

    @Value(value = "${mongo.db_host}")
    private String mongoDbHost;


    // ===== Beans configuration =====


    @Bean
    public Database getDatabase() {
        // Create the database singleton in the invoked dir
        return new Database(System.getProperty("user.dir"));
    }

    @Bean
    public PureIndexDb getPureIndexDb() {
        return new PureIndexDb();
    }

    @Bean
    public MongoIndexDb getMongoIndexDb() {
        return new MongoIndexDb();
    }

    @Bean
    public MongoClient mongo() {
        // Configure the mongoDB client
        ConnectionString connectionString = new ConnectionString("mongodb://" + mongoDbHost + "/" + mongoDbName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        // Get the mongo template to perform advanced request
        return new MongoTemplate(mongo(), mongoDbName);
    }

    @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
                if(debug) {
                    registry.addMapping("/**")
                            .allowedOrigins("http://localhost:9000")
                            .allowCredentials(true);
                }
			}
		};
	}

}
