package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.application.model.File;
import com.application.model.User;

/**
 * Repository interface for managing {@link File} entities in MongoDB.
 * 
 * This interface extends {@link MongoRepository} and provides methods
 * for performing file operations such as finding files by name or user.
 */
@Repository
public interface FileRepository extends MongoRepository<File, String> {

    /**
     * Finds a file by its name.
     *
     * @param name the name of the file to search for
     * @return Optional containing the found {@link File} or an empty Optional if not found
     */
    Optional<File> findByName(String name); // Method for finding a file by its name

    /**
     * Finds all files associated with a specific user.
     *
     * @param user the {@link User} whose files are to be retrieved
     * @return a list of {@link File} objects associated with the specified user
     */
    List<File> findByUser(User user); // Method for retrieving files by user
}
