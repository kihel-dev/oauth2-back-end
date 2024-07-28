package com.application.service;

import org.springframework.transaction.annotation.Transactional;
import com.application.dto.UserDTO;

/**
 * Service interface for managing user operations.
 *
 * This service provides methods for retrieving and saving users,
 * encapsulating business logic related to User entities.
 */
public interface UserService {

    /**
     * Retrieves a User by their email.
     *
     * @param email the email associated with the User
     * @return a {@link UserDTO} containing the user's details, or {@code null} if the user is not found
     */
    UserDTO getUserByEmail(String email);

    /**
     * Saves a User to the database.
     *
     * @param userDTO the {@link UserDTO} object containing user details
     */
    @Transactional
    void saveUser(UserDTO userDTO);
}
