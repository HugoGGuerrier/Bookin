package fr.bookin.bookin_back.database.index.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This interface creates the repository for the mongodb information
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface MongoInfoRepository extends MongoRepository<MongoInfo, Integer> {}
