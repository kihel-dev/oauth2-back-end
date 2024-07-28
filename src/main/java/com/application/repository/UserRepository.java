package com.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.application.model.User;

/**
 * Repository interface for performing CRUD operations on User entities.
 * 
 * This interface extends {@link MongoRepository}, providing various methods to interact with the database,
 * such as saving, finding, deleting, and updating User entities.
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a User by their email address.
     *
     * @param email the email address to search for
     * @return the User associated with the given email, or null if no User found
     */
    User findByEmail(String email);
}
