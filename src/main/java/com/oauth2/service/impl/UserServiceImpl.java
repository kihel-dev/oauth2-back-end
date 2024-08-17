package com.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.dto.UserDTO;
import com.oauth2.mapper.UserMapper;
import com.oauth2.model.User;
import com.oauth2.repository.UserRepository;
import com.oauth2.service.UserService;

/**
 * Implementation of the {@link UserService} interface.
 * 
 * This service manages user operations, including retrieving and saving users.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Retrieves a User by their email.
     *
     * @param email the email associated with the User
     * @return a {@link UserDTO} containing the user's details, or {@code null} if the user is not found
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        logger.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email);
        
        // Check if user exists
        if (user == null) {
            logger.warn("User not found for email: {}", email);
            return null; // Return null if user is not found; consider using exceptions for better error handling
        }
        
        UserDTO userDTO = userMapper.toDTO(user);
        logger.info("User found: {}", userDTO);
        return userDTO;
    }

    /**
     * Saves a User to the database.
     *
     * @param userDTO the {@link UserDTO} object containing user details
     */
    @Override
    public void saveUser(UserDTO userDTO) {
        logger.info("Saving user: {}", userDTO);
        User user = userMapper.toModel(userDTO);
        userRepository.save(user);
        logger.info("User saved successfully: {}", userDTO);
    }
}
