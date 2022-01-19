package fr.bookin.bookin_back.database.index.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This interface represents the repository for the index in the mongodb database
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface MongoIndexRepository extends MongoRepository<MongoIndex, Integer> {}
